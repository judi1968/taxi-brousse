import psycopg2
import os
import re

# --- Configuration PostgreSQL ---
config_pg = {
    "host": "localhost",
    "dbname": "taxi_brousse",
    "user": "postgres",
    "password": "mdpprom15",
    "port": 5432
}

def to_camel_case(name):
    """Convertit un nom de colonne en camelCase"""
    parts = name.split('_')
    return parts[0] + ''.join(part.capitalize() for part in parts[1:])

def to_pascal_case(name):
    """Convertit un nom de table en PascalCase"""
    parts = name.split('_')
    return ''.join(part.capitalize() for part in parts)

def extract_name_no_id(field_name: str) -> str:
    """
    Transforme:
    - IdPersonne        -> personne
    - id_personne       -> personne
    - IdIdentifiant     -> identifiant
    - IdVoyageVoiture   -> voyage_voiture
    - idVoyageVoiture   -> voyage_voiture
    """
    # 1. Supprimer uniquement le Id / id_ au début
    name = re.sub(r'^id_?', '', field_name, flags=re.IGNORECASE)
    # 2. Si CamelCase → snake_case
    name = re.sub(r'(?<!^)(?=[A-Z])', '_', name)
    # 3. Tout en minuscule
    return name.lower()

def get_java_type(postgres_type):
    """Convertit le type PostgreSQL en type Java pour les modèles"""
    type_mapping = {
        'integer': 'Integer',
        'bigint': 'Long',
        'double precision': 'Double',
        'boolean': 'Boolean',
        'character varying': 'String',
        'text': 'String',
        'timestamp without time zone': 'Date',
        'date': 'Date',
        'time without time zone': 'Time'
    }
    return type_mapping.get(postgres_type, 'String')

def get_simple_java_type(postgres_type):
    """Convertit le type PostgreSQL en type Java simple pour les DTOs"""
    type_mapping = {
        'integer': 'int',
        'bigint': 'long',
        'double precision': 'double',
        'boolean': 'boolean',
        'character varying': 'String',
        'text': 'String',
        'timestamp without time zone': 'String',
        'date': 'String',
        'time without time zone': 'String'
    }
    return type_mapping.get(postgres_type, 'String')

def generate_model_file(table_name, columns, primary_keys, foreign_keys, output_dir, model_package):
    """Génère un fichier de modèle Java avec annotations"""
    class_name = to_pascal_case(table_name)
    # Créer les sous-dossiers selon le package
    package_path = model_package.replace('.', '/')
    full_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(full_output_dir, exist_ok=True)
    
    file_path = os.path.join(full_output_dir, f"{class_name}.java")
    
    with open(file_path, 'w', encoding='utf-8') as f:
        # Package
        f.write(f"package {model_package};\n\n")
        
        # Imports nécessaires
        imports = set(['import com.project.pja.databases.generalisation.annotation.AttributDb;',
                      'import com.project.pja.databases.generalisation.annotation.IdDb;',
                      'import com.project.pja.databases.generalisation.annotation.TableDb;'])
        
        # Ajouter les imports pour les types
        for col_name, col_type in columns:
            java_type = get_java_type(col_type)
            if java_type == 'Date':
                imports.add('import java.util.Date;')
            elif java_type == 'Time':
                imports.add('import java.sql.Time;')
        
        # Ajouter les imports pour les foreign keys
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            imports.add(f'import {model_package}.{to_pascal_case(fk_table)};')
        
        # Écrire les imports triés
        for imp in sorted(imports):
            f.write(f"{imp}\n")
        
        # Annotation de table
        f.write(f"\n@TableDb(name = \"{table_name}\")\n")
        f.write(f"public class {class_name} {{\n\n")
        
        # Déclaration des attributs
        for col_name, col_type in columns:
            java_type = get_java_type(col_type)
            field_name = to_camel_case(col_name)
            
            # Vérifier si c'est une clé étrangère
            fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
            
            if col_name in primary_keys:
                f.write("    @IdDb\n")
                f.write(f'    @AttributDb(name = "{col_name}")\n')
                f.write(f"    private {java_type} {field_name};\n\n")
            elif fk_info:
                # Pour les foreign keys, on utilise l'objet référencé
                fk_table_name = fk_info[1]
                referenced_class = to_pascal_case(fk_table_name)
                object_field_name = extract_name_no_id(field_name)
                f.write(f'    @AttributDb(name = "{col_name}")\n')
                f.write(f"    private {referenced_class} {object_field_name}; // ← objet {referenced_class}\n\n")
            else:
                f.write(f'    @AttributDb(name = "{col_name}")\n')
                f.write(f"    private {java_type} {field_name};\n\n")
        
        # Constructeur par défaut
        f.write("    public {}() {{\n".format(class_name))
        f.write("    }\n\n")
        
        # Constructeur avec paramètres
        constructor_params = ["int id"]
        
        # Paramètres pour les attributs non-clés primaires
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                field_name = to_camel_case(col_name)
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                
                if fk_info:
                    fk_table_name = fk_info[1]
                    referenced_class = to_pascal_case(fk_table_name)
                    object_field_name = extract_name_no_id(field_name)
                    constructor_params.append(f"{referenced_class} {object_field_name}")
                else:
                    java_type = get_java_type(col_type)
                    constructor_params.append(f"{java_type} {field_name}")
        
        f.write("    public {}({}) {{\n".format(class_name, ", ".join(constructor_params)))
        f.write("        this.id = id;\n")
        
        # Initialisation des autres attributs
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                field_name = to_camel_case(col_name)
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                
                if fk_info:
                    fk_table_name = fk_info[1]
                    object_field_name = extract_name_no_id(field_name)
                    f.write(f"        this.{object_field_name} = {object_field_name};\n")
                else:
                    f.write(f"        this.{field_name} = {field_name};\n")
        
        f.write("    }\n\n")
        
        # Getters et Setters
        for col_name, col_type in columns:
            java_type = get_java_type(col_type)
            field_name = to_camel_case(col_name)
            
            fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
            
            if fk_info:
                # Pour les foreign keys
                fk_table_name = fk_info[1]
                referenced_class = to_pascal_case(fk_table_name)
                object_field_name = extract_name_no_id(field_name)
                getter_name = object_field_name[0].upper() + object_field_name[1:]
                
                # Getter
                f.write(f"    public {referenced_class} get{getter_name}() {{\n")
                f.write(f"        return this.{object_field_name};\n")
                f.write("    }\n\n")
                
                # Setter
                f.write(f"    public void set{getter_name}({referenced_class} {object_field_name}) {{\n")
                f.write(f"        this.{object_field_name} = {object_field_name};\n")
                f.write("    }\n\n")
            else:
                # Pour les attributs normaux
                method_name = field_name[0].upper() + field_name[1:]
                
                # Getter avec conversion pour types primitifs
                if java_type in ['Integer', 'Long', 'Double', 'Boolean']:
                    simple_type = get_simple_java_type(col_type)
                    f.write(f"    public {simple_type} get{method_name}() {{\n")
                    f.write(f"        return this.{field_name} != null ? this.{field_name}.{simple_type}Value() : 0;\n")
                    f.write("    }\n\n")
                    
                    # Getter qui retourne l'objet wrapper
                    f.write(f"    public {java_type} get{method_name}Object() {{\n")
                    f.write(f"        return this.{field_name};\n")
                    f.write("    }\n\n")
                else:
                    f.write(f"    public {java_type} get{method_name}() {{\n")
                    f.write(f"        return this.{field_name};\n")
                    f.write("    }\n\n")
                
                # Setter
                if java_type == 'String':
                    f.write(f"    public void set{method_name}({java_type} {field_name}) {{\n")
                    f.write(f"        if ({field_name} != null && {field_name}.trim().length() > 0)\n")
                    f.write(f"            this.{field_name} = {field_name};\n")
                    f.write("    }\n\n")
                else:
                    f.write(f"    public void set{method_name}({java_type} {field_name}) {{\n")
                    f.write(f"        this.{field_name} = {field_name};\n")
                    f.write("    }\n\n")
        
        f.write("}\n")
    
    print(f"Modèle généré: {file_path}")

def generate_dto_file(table_name, columns, primary_keys, foreign_keys, output_dir, dto_package):
    """Génère un fichier DTO Java"""
    class_name = to_pascal_case(table_name)
    dto_name = f"{class_name}DTO"
    
    # Créer les sous-dossiers selon le package
    package_path = dto_package.replace('.', '/')
    full_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(full_output_dir, exist_ok=True)
    
    file_path = os.path.join(full_output_dir, f"{dto_name}.java")
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(f"package {dto_package};\n\n")
        f.write(f"public class {dto_name} {{\n")
        
        # Déclaration des attributs (sauf clés primaires)
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                java_type = get_simple_java_type(col_type)
                field_name = to_camel_case(col_name)
                
                # Pour les foreign keys, on stocke l'ID
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                if fk_info:
                    fk_id_type = get_simple_java_type(next(col_type for col_name2, col_type2 in columns if col_name2 == col_name))
                    object_field_name = extract_name_no_id(field_name)
                    field_name = f"{object_field_name}Id"
                    f.write(f"    private {fk_id_type} {field_name};\n")
                else:
                    f.write(f"    private {java_type} {field_name};\n")
        
        f.write("\n")
        
        # Constructeur par défaut
        f.write("    public {}() {{\n".format(dto_name))
        f.write("    }\n\n")
        
        # Constructeur avec paramètres
        constructor_params = []
        
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                field_name = to_camel_case(col_name)
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                
                if fk_info:
                    fk_id_type = get_simple_java_type(next(col_type for col_name2, col_type2 in columns if col_name2 == col_name))
                    object_field_name = extract_name_no_id(field_name)
                    field_name = f"{object_field_name}Id"
                    constructor_params.append(f"{fk_id_type} {field_name}")
                else:
                    java_type = get_simple_java_type(col_type)
                    constructor_params.append(f"{java_type} {field_name}")
        
        if constructor_params:
            f.write("    public {}({}) {{\n".format(dto_name, ", ".join(constructor_params)))
            
            for col_name, col_type in columns:
                if col_name not in primary_keys:
                    field_name = to_camel_case(col_name)
                    fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                    
                    if fk_info:
                        object_field_name = extract_name_no_id(field_name)
                        field_name = f"{object_field_name}Id"
                        f.write(f"        this.{field_name} = {field_name};\n")
                    else:
                        f.write(f"        this.{field_name} = {field_name};\n")
            
            f.write("    }\n\n")
        
        # Getters et Setters
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                field_name = to_camel_case(col_name)
                
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                if fk_info:
                    object_field_name = extract_name_no_id(field_name)
                    field_name = f"{object_field_name}Id"
                    method_name = object_field_name[0].upper() + object_field_name[1:] + "Id"
                    fk_id_type = get_simple_java_type(next(col_type for col_name2, col_type2 in columns if col_name2 == col_name))
                    
                    # Getter
                    f.write(f"    public {fk_id_type} get{method_name}() {{\n")
                    f.write(f"        return {field_name};\n")
                    f.write("    }\n\n")
                    
                    # Setter
                    f.write(f"    public void set{method_name}({fk_id_type} {field_name}) {{\n")
                    f.write(f"        this.{field_name} = {field_name};\n")
                    f.write("    }\n\n")
                else:
                    method_name = field_name[0].upper() + field_name[1:]
                    java_type = get_simple_java_type(col_type)
                    
                    # Getter
                    f.write(f"    public {java_type} get{method_name}() {{\n")
                    f.write(f"        return {field_name};\n")
                    f.write("    }\n\n")
                    
                    # Setter
                    f.write(f"    public void set{method_name}({java_type} {field_name}) {{\n")
                    f.write(f"        this.{field_name} = {field_name};\n")
                    f.write("    }\n\n")
        
        f.write("}\n")
    
    print(f"DTO généré: {file_path}")

def generate_controller_file(table_name, columns, primary_keys, foreign_keys, output_dir, 
                            controller_package, model_package, dto_package):
    """Génère un fichier contrôleur Spring avec goToCreate"""
    class_name = to_pascal_case(table_name)
    controller_name = f"{class_name}Controller"
    dto_name = f"{class_name}DTO"
    
    # Créer les sous-dossiers selon le package
    package_path = controller_package.replace('.', '/')
    full_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(full_output_dir, exist_ok=True)
    
    file_path = os.path.join(full_output_dir, f"{controller_name}.java")
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(f"package {controller_package};\n\n")
        f.write("import java.sql.Connection;\n")
        f.write("import java.util.List;\n\n")
        f.write("import org.springframework.stereotype.Controller;\n")
        f.write("import org.springframework.ui.Model;\n")
        f.write("import org.springframework.web.bind.annotation.GetMapping;\n")
        f.write("import org.springframework.web.bind.annotation.ModelAttribute;\n")
        f.write("import org.springframework.web.bind.annotation.PostMapping;\n\n")
        f.write(f"import {dto_package}.{dto_name};\n")
        f.write(f"import {model_package}.{class_name};\n")
        
        # Importer tous les modèles référencés par les foreign keys
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            f.write(f"import {model_package}.{to_pascal_case(fk_table)};\n")
        
        f.write("import com.project.pja.databases.MyConnection;\n")
        f.write("import com.project.pja.databases.generalisation.DB;\n\n")
        
        f.write("@Controller\n")
        f.write(f"public class {controller_name} {{\n\n")
        
        # Méthode goToCreate pour charger les données nécessaires avant création
        create_method_name = f"creation{class_name}" if len(table_name) > 1 else table_name
        f.write(f"    @GetMapping(\"/{create_method_name}\")\n")
        f.write(f"    public String goToCreate(Model model) {{\n")
        f.write("        Connection connection = null;\n")
        f.write("        try {\n")
        f.write("            connection = MyConnection.connect();\n\n")
        
        # Charger les listes des tables référencées par les foreign keys
        fk_tables_processed = set()
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            if fk_table not in fk_tables_processed:
                fk_class_name = to_pascal_case(fk_table)
                f.write(f"\n")
                f.write(f"            // Récupérer la liste des {fk_table}s\n")
                f.write(f"            List<{fk_class_name}> {fk_table}s = (List<{fk_class_name}>) DB.getAll(new {fk_class_name}(), connection);\n")
                f.write(f"            model.addAttribute(\"{fk_table}s\", {fk_table}s);\n")
                fk_tables_processed.add(fk_table)
        
        if not fk_tables_processed:
            f.write("\n            // Aucune donnée à charger\n")
        
        f.write(f"\n            // Initialiser un DTO vide pour le formulaire\n")
        f.write(f"            model.addAttribute(\"{table_name}DTO\", new {dto_name}());\n\n")
        f.write("        } catch (Exception e) {\n")
        f.write("            e.printStackTrace();\n")
        f.write("            model.addAttribute(\"error\", \"Erreur lors du chargement des données: \" + e.getMessage());\n")
        f.write("        } finally {\n")
        f.write("            if (connection != null) {\n")
        f.write("                try {\n")
        f.write("                    connection.close();\n")
        f.write("                } catch (Exception e) {\n")
        f.write("                    e.printStackTrace();\n")
        f.write("                }\n")
        f.write("            }\n")
        f.write("        }\n")
        f.write(f"        return \"pages/{table_name}/creation\";\n")
        f.write("    }\n\n")
        
        # Méthode POST pour sauvegarder
        f.write(f"    @PostMapping(\"/save{class_name}\")\n")
        f.write(f"    public String save{class_name}(@ModelAttribute {dto_name} {table_name}DTO, Model model) {{\n")
        f.write("        \n")
        f.write("        Connection connection = null;\n")
        f.write("        try {\n")
        f.write("            // Établir la connexion\n")
        f.write("            connection = MyConnection.connect();\n")
        f.write("            \n")
        f.write(f"            // Créer l'objet {class_name} à partir du DTO\n")
        f.write(f"            {class_name} {table_name} = new {class_name}();\n")
        
        # Attribution des valeurs depuis le DTO
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                field_name = to_camel_case(col_name)
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                
                if fk_info:
                    # Pour les foreign keys, on récupère l'objet complet
                    fk_table_name = fk_info[1]
                    referenced_class = to_pascal_case(fk_table_name)
                    object_field_name = extract_name_no_id(field_name)
                    dto_field_name = f"{object_field_name}Id"
                    f.write(f"            \n")
                    f.write(f"            // Récupération de l'objet {referenced_class}\n")
                    f.write(f"            if ({table_name}DTO.get{referenced_class}Id() != 0) {{\n")
                    f.write(f"                {referenced_class} {object_field_name} = {referenced_class}.getById({table_name}DTO.get{referenced_class}Id(), connection);\n")
                    f.write(f"                {table_name}.set{referenced_class}({object_field_name});\n")
                    f.write("            }\n")
                else:
                    method_name = field_name[0].upper() + field_name[1:]
                    java_type = get_simple_java_type(col_type)
                    if java_type in ['int', 'long', 'double', 'boolean']:
                        f.write(f"            {table_name}.set{method_name}({table_name}DTO.get{method_name}());\n")
                    else:
                        f.write(f"            {table_name}.set{method_name}({table_name}DTO.get{method_name}());\n")
        
        f.write("            \n")
        f.write("            // Sauvegarder dans la base de données\n")
        f.write(f"            DB.save({table_name}, connection);\n")
        f.write("            \n")
        f.write("            // Message de succès\n")
        f.write(f"            model.addAttribute(\"success\", \"{class_name} enregistré avec succès !\");\n")
        f.write(f"            model.addAttribute(\"{table_name}DTO\", new {dto_name}()); // Réinitialiser le formulaire\n")
        f.write("            \n")
        f.write("            // Recharger les listes pour les foreign keys\n")
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            fk_class_name = to_pascal_case(fk_table)
            f.write(f"            List<{fk_class_name}> {fk_table}s = (List<{fk_class_name}>) DB.getAll(new {fk_class_name}(), connection);\n")
            f.write(f"            model.addAttribute(\"{fk_table}s\", {fk_table}s);\n")
        
        f.write("            \n")
        f.write("        } catch (Exception e) {\n")
        f.write("            e.printStackTrace();\n")
        f.write("            model.addAttribute(\"error\", \"Erreur lors de l'enregistrement : \" + e.getMessage());\n")
        f.write(f"            model.addAttribute(\"{table_name}DTO\", {table_name}DTO); // Garder les données saisies\n")
        f.write("            \n")
        f.write("            // Recharger les listes en cas d'erreur\n")
        f.write("            try {\n")
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            fk_class_name = to_pascal_case(fk_table)
            f.write(f"                List<{fk_class_name}> {fk_table}s = (List<{fk_class_name}>) DB.getAll(new {fk_class_name}(), connection);\n")
            f.write(f"                model.addAttribute(\"{fk_table}s\", {fk_table}s);\n")
        f.write("            } catch (Exception ex) {\n")
        f.write("                ex.printStackTrace();\n")
        f.write("            }\n")
        f.write("        } finally {\n")
        f.write("            if (connection != null) {\n")
        f.write("                try {\n")
        f.write("                    connection.close();\n")
        f.write("                } catch (Exception e) {\n")
        f.write("                    e.printStackTrace();\n")
        f.write("                }\n")
        f.write("            }\n")
        f.write("        }\n")
        f.write("        \n")
        f.write(f"        return \"pages/{table_name}/creation\";\n")
        f.write("    }\n\n")
        
        # Méthode GET pour lister
        f.write(f"    @GetMapping(\"/liste{class_name}\")\n")
        f.write(f"    public String liste{class_name}(Model model) {{\n")
        f.write("        Connection connection = null;\n")
        f.write("        try {\n")
        f.write("            connection = MyConnection.connect();\n")
        f.write("\n")
        f.write(f"            // Récupérer la liste des {table_name}s depuis la base\n")
        f.write(f"            List<{class_name}> {table_name}s = (List<{class_name}>) DB.getAll(new {class_name}(), connection);\n")
        f.write("\n")
        f.write(f"            // Ajouter la liste des {table_name}s au modèle\n")
        f.write(f"            model.addAttribute(\"{table_name}s\", {table_name}s);\n")
        f.write("\n")
        f.write("        } catch (Exception e) {\n")
        f.write("            e.printStackTrace();\n")
        f.write(f"            model.addAttribute(\"error\", \"Erreur lors du chargement des {table_name}s: \" + e.getMessage());\n")
        f.write("        } finally {\n")
        f.write("            if (connection != null) {\n")
        f.write("                try {\n")
        f.write("                    connection.close();\n")
        f.write("                } catch (Exception e) {\n")
        f.write("                    e.printStackTrace();\n")
        f.write("                }\n")
        f.write("            }\n")
        f.write("        }\n")
        f.write(f"        return \"pages/{table_name}/liste{class_name}\";\n")
        f.write("    }\n")
        
        f.write("}\n")
    
    print(f"Contrôleur généré: {file_path}")

def generate_jsp_list_file(table_name, columns, primary_keys, foreign_keys, output_dir, jsp_base_path):
    """Génère un fichier JSP pour lister les éléments"""
    class_name = to_pascal_case(table_name)
    
    # Créer le répertoire pour le JSP
    jsp_dir = os.path.join(output_dir, f"pages/{table_name}")
    os.makedirs(jsp_dir, exist_ok=True)
    
    file_path = os.path.join(jsp_dir, f"liste{class_name}.jsp")
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write('<%@ page contentType="text/html;charset=UTF-8" %>\n')
        f.write('<%@ page import="java.util.List" %>\n')
        f.write(f'<%@ page import="com.project.model.table.{class_name}" %>\n')
        
        # Ajouter l'import pour Date si nécessaire
        has_date = any(get_java_type(col_type) == 'Date' for col_name, col_type in columns)
        if has_date:
            f.write('<%@ page import="java.text.SimpleDateFormat" %>\n')
        
        f.write('<%\n')
        f.write(f'    List<{class_name}> {table_name}s = (List<{class_name}>) request.getAttribute("{table_name}s");\n')
        f.write('    String error = (String) request.getAttribute("error");\n')
        if has_date:
            f.write('    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");\n')
        f.write('%>\n')
        f.write('<!DOCTYPE html>\n')
        f.write('<html lang="en">\n')
        f.write('  <head>\n')
        f.write('    <meta charset="utf-8">\n')
        f.write('    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">\n')
        f.write(f'    <title>Liste des {table_name}s</title>\n')
        f.write('    <%@ include file="../../includes/css.jsp" %>\n')
        f.write('  </head>\n')
        f.write('  <body>\n')
        f.write('    <div class="container-scroller">\n')
        f.write('      <%@ include file="../../includes/navbar.jsp" %>\n')
        f.write('      <div class="container-fluid page-body-wrapper">\n')
        f.write('        <%@ include file="../../includes/header.jsp" %>\n')
        f.write('        <div class="main-panel">\n')
        f.write('          <div class="content-wrapper">\n')
        f.write('            <div class="page-header">\n')
        f.write(f'              <h3 class="page-title">Liste des {table_name}s</h3>\n')
        f.write('            </div>\n')
        f.write('            \n')
        f.write('            <% if (error != null) { %>\n')
        f.write('            <div class="alert alert-danger alert-dismissible fade show" role="alert">\n')
        f.write('              <%= error %>\n')
        f.write('              <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n')
        f.write('                <span aria-hidden="true">&times;</span>\n')
        f.write('              </button>\n')
        f.write('            </div>\n')
        f.write('            <% } %>\n')
        f.write('            \n')
        f.write('            <div class="row">\n')
        f.write('              <div class="col-lg-12 grid-margin stretch-card">\n')
        f.write('                <div class="card">\n')
        f.write('                  <div class="card-body">\n')
        f.write(f'                    <h4 class="card-title">Liste des {table_name}s</h4>\n')
        f.write(f'                    <p class="card-description">Tous les {table_name}s disponibles</p>\n')
        f.write('                    <div class="table-responsive">\n')
        f.write('                      <table class="table table-hover table-striped">\n')
        f.write('                        <thead>\n')
        f.write('                          <tr>\n')
        
        # En-têtes de colonnes
        for col_name, col_type in columns:
            field_name = to_camel_case(col_name)
            f.write(f'                            <th>{field_name[0].upper()}{field_name[1:]}</th>\n')
        
        f.write('                          </tr>\n')
        f.write('                        </thead>\n')
        f.write('                        <tbody>\n')
        f.write(f'                          <% if ({table_name}s != null && !{table_name}s.isEmpty()) {{\n')
        f.write(f'                               for ({class_name} {table_name} : {table_name}s) {{ \n')
        f.write('%>\n')
        f.write('                          <tr>\n')
        
        # Contenu des colonnes
        for col_name, col_type in columns:
            field_name = to_camel_case(col_name)
            
            fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
            
            if fk_info:
                # Pour les foreign keys, afficher le nom de l'objet lié
                fk_table_name = fk_info[1]
                referenced_class = to_pascal_case(fk_table_name)
                object_field_name = extract_name_no_id(field_name)
                getter_name = object_field_name[0].upper() + object_field_name[1:]
                
                f.write(f'                            <td>\n')
                f.write(f'                              <% if ({table_name}.get{getter_name}() != null) {{ %>\n')
                f.write(f'                                <%= {table_name}.get{getter_name}().getNom() %>\n')
                f.write('                              <% } else { %>\n')
                f.write('                                Non défini\n')
                f.write('                              <% } %>\n')
                f.write('                            </td>\n')
            elif get_java_type(col_type) == 'Date':
                method_name = field_name[0].upper() + field_name[1:]
                f.write(f'                            <td>\n')
                f.write(f'                              <% if ({table_name}.get{method_name}Object() != null) {{ %>\n')
                f.write(f'                                <%= sdf.format({table_name}.get{method_name}Object()) %>\n')
                f.write('                              <% } else { %>\n')
                f.write('                                Non défini\n')
                f.write('                              <% } %>\n')
                f.write('                            </td>\n')
            else:
                method_name = field_name[0].upper() + field_name[1:]
                f.write(f'                            <td><%= {table_name}.get{method_name}() %></td>\n')
        
        f.write('                          </tr>\n')
        f.write('<% }\n')
        f.write('                             } else { %>\n')
        f.write('                          <tr>\n')
        f.write(f'                            <td colspan="{len(columns)}" class="text-center">Aucun {table_name} trouvé</td>\n')
        f.write('                          </tr>\n')
        f.write('                          <% } %>\n')
        f.write('                        </tbody>\n')
        f.write('                      </table>\n')
        f.write('                    </div>\n')
        f.write('                  </div>\n')
        f.write('                </div>\n')
        f.write('              </div>\n')
        f.write('            </div>\n')
        f.write('          </div>\n')
        f.write('          <%@ include file="../../includes/footer.jsp" %>\n')
        f.write('        </div>\n')
        f.write('      </div>\n')
        f.write('    </div>\n')
        f.write('    <%@ include file="../../includes/js.jsp" %>\n')
        f.write('  </body>\n')
        f.write('</html>\n')
    
    print(f"JSP liste généré: {file_path}")

def generate_jsp_create_file(table_name, columns, primary_keys, foreign_keys, output_dir, jsp_base_path):
    """Génère un fichier JSP pour la création"""
    class_name = to_pascal_case(table_name)
    
    # Créer le répertoire pour le JSP
    jsp_dir = os.path.join(output_dir, f"pages/{table_name}")
    os.makedirs(jsp_dir, exist_ok=True)
    
    file_path = os.path.join(jsp_dir, f"creation.jsp")
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write('<%@ page contentType="text/html;charset=UTF-8" %>\n')
        f.write('<%@ page import="java.util.List" %>\n')
        
        # Importer les modèles nécessaires pour les select
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            f.write(f'<%@ page import="com.project.model.table.{to_pascal_case(fk_table)}" %>\n')
        
        f.write('<%\n')
        f.write('    String success = (String) request.getAttribute("success");\n')
        f.write('    String error = (String) request.getAttribute("error");\n')
        
        # Variables pour les select
        for fk_col, fk_table, fk_ref_col in foreign_keys:
            f.write(f'    List<{to_pascal_case(fk_table)}> {fk_table}s = (List<{to_pascal_case(fk_table)}>) request.getAttribute("{fk_table}s");\n')
        
        f.write('%>\n')
        f.write('<!DOCTYPE html>\n')
        f.write('<html lang="en">\n')
        f.write('  <head>\n')
        f.write('    <meta charset="utf-8">\n')
        f.write('    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">\n')
        f.write(f'    <title>Creation {class_name}</title>\n')
        f.write('    <%@ include file="../../includes/css.jsp" %>\n')
        f.write('  </head>\n')
        f.write('  <body>\n')
        f.write('    <div class="container-scroller">\n')
        f.write('      <%@ include file="../../includes/navbar.jsp" %>\n')
        f.write('      <div class="container-fluid page-body-wrapper">\n')
        f.write('        <%@ include file="../../includes/header.jsp" %>\n')
        f.write('        <div class="main-panel">\n')
        f.write('          <div class="content-wrapper">\n')
        f.write('            <div class="page-header">\n')
        f.write(f'              <h3 class="page-title">Creation {class_name}</h3>\n')
        f.write('            </div>\n')
        f.write('            \n')
        f.write('            <!-- Messages d\'alerte -->\n')
        f.write('            <% if (success != null) { %>\n')
        f.write('            <div class="alert alert-success alert-dismissible fade show" role="alert">\n')
        f.write('              <%= success %>\n')
        f.write('              <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n')
        f.write('                <span aria-hidden="true">&times;</span>\n')
        f.write('              </button>\n')
        f.write('            </div>\n')
        f.write('            <% } %>\n')
        f.write('            \n')
        f.write('            <% if (error != null) { %>\n')
        f.write('            <div class="alert alert-danger alert-dismissible fade show" role="alert">\n')
        f.write('              <%= error %>\n')
        f.write('              <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n')
        f.write('                <span aria-hidden="true">&times;</span>\n')
        f.write('              </button>\n')
        f.write('            </div>\n')
        f.write('            <% } %>\n')
        f.write('            \n')
        f.write('            <div class="row">\n')
        f.write('              <div class="col-md-12 grid-margin stretch-card">\n')
        f.write('                <div class="card">\n')
        f.write('                  <div class="card-body">\n')
        f.write(f'                    <h4 class="card-title">Création de {class_name}</h4>\n')
        f.write(f'                    <p class="card-description">Ajouter un nouveau {table_name}</p>\n')
        f.write(f'                    <form class="forms-sample" action="save{class_name}" method="post">\n')
        f.write('                      <div class="row">\n')
        f.write('                        <div class="col-md-6">\n')
        
        # Champs du formulaire
        field_count = 0
        for col_name, col_type in columns:
            if col_name not in primary_keys:
                field_name = to_camel_case(col_name)
                display_name = field_name[0].upper() + field_name[1:]
                fk_info = next((fk for fk in foreign_keys if fk[0] == col_name), None)
                
                f.write('                          <div class="form-group row">\n')
                
                if fk_info:
                    # Select pour foreign key
                    fk_table_name = fk_info[1]
                    object_field_name = extract_name_no_id(field_name)
                    display_name = object_field_name[0].upper() + object_field_name[1:]
                    
                    f.write(f'                            <label for="{object_field_name}" class="col-sm-3 col-form-label">{display_name}</label>\n')
                    f.write('                            <div class="col-sm-9">\n')
                    f.write(f'                              <select class="form-control" id="{object_field_name}" name="{object_field_name}Id" required>\n')
                    f.write(f'                                <option value="">Sélectionnez un {display_name}</option>\n')
                    f.write(f'                                <% if ({fk_table_name}s != null) {{\n')
                    f.write(f'                                    for ({to_pascal_case(fk_table_name)} {fk_table_name} : {fk_table_name}s) {{ \n')
                    f.write('%>\n')
                    f.write(f'                                <option value="<%= {fk_table_name}.getId() %>"> \n')
                    f.write('                                       \n')
                    f.write(f'                                  <%= {fk_table_name}.getNom() != null ? {fk_table_name}.getNom() : "ID: " + {fk_table_name}.getId() %>\n')
                    f.write('                                </option>\n')
                    f.write('<% }\n')
                    f.write('                                } %>\n')
                    f.write('                              </select>\n')
                else:
                    # Input standard
                    f.write(f'                            <label for="{field_name}" class="col-sm-3 col-form-label">{display_name}</label>\n')
                    f.write('                            <div class="col-sm-9">\n')
                    
                    java_type = get_java_type(col_type)
                    if java_type == 'Date':
                        f.write(f'                              <input type="datetime-local" class="form-control" id="{field_name}" name="{field_name}" required>\n')
                    elif java_type in ['Integer', 'Long', 'Double']:
                        f.write(f'                              <input type="number" class="form-control" id="{field_name}" name="{field_name}" required>\n')
                    elif java_type == 'Boolean':
                        f.write(f'                              <select class="form-control" id="{field_name}" name="{field_name}" required>\n')
                        f.write('                                <option value="true">Oui</option>\n')
                        f.write('                                <option value="false">Non</option>\n')
                        f.write('                              </select>\n')
                    else:
                        f.write(f'                              <input type="text" class="form-control" id="{field_name}" name="{field_name}" required>\n')
                
                f.write('                            </div>\n')
                f.write('                          </div>\n')
                field_count += 1
        
        f.write('                        </div>\n')
        f.write('                      </div>\n')
        f.write('                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>\n')
        f.write(f'                      <a href="liste{class_name}" class="btn btn-dark">Annuler</a>\n')
        f.write('                    </form>\n')
        f.write('                  </div>\n')
        f.write('                </div>\n')
        f.write('              </div>\n')
        f.write('            </div>\n')
        f.write('          </div>\n')
        f.write('          <%@ include file="../../includes/footer.jsp" %>\n')
        f.write('        </div>\n')
        f.write('      </div>\n')
        f.write('    </div>\n')
        f.write('    <%@ include file="../../includes/js.jsp" %>\n')
        f.write('  </body>\n')
        f.write('</html>\n')
    
    print(f"JSP création généré: {file_path}")

def main():
    # --- CONFIGURATION DES PACKAGES ET RÉPERTOIRES ---
    # Vous pouvez modifier ces valeurs selon vos besoins
    
    # Packages Java
    MODEL_PACKAGE = "com.project.model.table"
    DTO_PACKAGE = "com.project.dto"
    CONTROLLER_PACKAGE = "com.project.controller"
    
    # Répertoires de sortie
    BASE_OUTPUT_DIR = "generated"  # Répertoire racine pour tous les fichiers générés
    
    # Sous-répertoires pour chaque type de fichier
    MODEL_OUTPUT_DIR = os.path.join(BASE_OUTPUT_DIR, "src/main/java")
    DTO_OUTPUT_DIR = os.path.join(BASE_OUTPUT_DIR, "src/main/java")
    CONTROLLER_OUTPUT_DIR = os.path.join(BASE_OUTPUT_DIR, "src/main/java")
    JSP_OUTPUT_DIR = os.path.join(BASE_OUTPUT_DIR, "src/main/webapp/WEB-INF/views")
    
    try:
        conn = psycopg2.connect(**config_pg)
        cur = conn.cursor()

        # Récupérer toutes les tables (sauf les vues)
        cur.execute("""
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = 'public'
            AND table_type = 'BASE TABLE'
            ORDER BY table_name;
        """)
        tables = cur.fetchall()

        for table_name, in tables:
            print(f"\nTraitement de la table: {table_name}")
            
            # Colonnes + types
            cur.execute("""
                SELECT column_name, data_type
                FROM information_schema.columns
                WHERE table_name = %s
                ORDER BY ordinal_position;
            """, (table_name,))
            columns = cur.fetchall()

            # Clés primaires
            cur.execute("""
                SELECT kcu.column_name
                FROM information_schema.table_constraints tc
                JOIN information_schema.key_column_usage kcu
                    ON tc.constraint_name = kcu.constraint_name
                WHERE tc.constraint_type = 'PRIMARY KEY'
                  AND tc.table_name = %s;
            """, (table_name,))
            primary_keys = [row[0] for row in cur.fetchall()]

            # Clés étrangères
            cur.execute("""
                SELECT
                    kcu.column_name,
                    ccu.table_name AS foreign_table,
                    ccu.column_name AS foreign_column
                FROM information_schema.table_constraints AS tc
                JOIN information_schema.key_column_usage AS kcu
                    ON tc.constraint_name = kcu.constraint_name
                JOIN information_schema.constraint_column_usage AS ccu
                    ON ccu.constraint_name = tc.constraint_name
                WHERE tc.constraint_type = 'FOREIGN KEY'
                  AND tc.table_name = %s;
            """, (table_name,))
            foreign_keys = cur.fetchall()

            # Générer les fichiers avec les packages spécifiés
            generate_model_file(table_name, columns, primary_keys, foreign_keys, 
                              MODEL_OUTPUT_DIR, MODEL_PACKAGE)
            generate_dto_file(table_name, columns, primary_keys, foreign_keys,
                            DTO_OUTPUT_DIR, DTO_PACKAGE)
            generate_controller_file(table_name, columns, primary_keys, foreign_keys,
                                   CONTROLLER_OUTPUT_DIR, CONTROLLER_PACKAGE,
                                   MODEL_PACKAGE, DTO_PACKAGE)
            generate_jsp_list_file(table_name, columns, primary_keys, foreign_keys,
                                 JSP_OUTPUT_DIR, "pages")
            generate_jsp_create_file(table_name, columns, primary_keys, foreign_keys,
                                   JSP_OUTPUT_DIR, "pages")

        print(f"\nGénération terminée!")
        print(f"Modèles: {MODEL_OUTPUT_DIR}/{MODEL_PACKAGE.replace('.', '/')}")
        print(f"DTOs: {DTO_OUTPUT_DIR}/{DTO_PACKAGE.replace('.', '/')}")
        print(f"Contrôleurs: {CONTROLLER_OUTPUT_DIR}/{CONTROLLER_PACKAGE.replace('.', '/')}")
        print(f"JSPs: {JSP_OUTPUT_DIR}/pages/")

    except Exception as e:
        print("Erreur PostgreSQL :", e)
    finally:
        if conn:
            cur.close()
            conn.close()

if __name__ == "__main__":
    main()