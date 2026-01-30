<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypeClient" %>
<%@ page import="com.project.model.table.TypeClient" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<TypeClient> type_clients = (List<TypeClient>) request.getAttribute("type_clients");
    List<TypeClient> type_clients = (List<TypeClient>) request.getAttribute("type_clients");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation ParametreCaclulPrixType</title>
    <%@ include file="../../includes/css.jsp" %>
  </head>
  <body>
    <div class="container-scroller">
      <%@ include file="../../includes/navbar.jsp" %>
      <div class="container-fluid page-body-wrapper">
        <%@ include file="../../includes/header.jsp" %>
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="page-header">
              <h3 class="page-title">Creation ParametreCaclulPrixType</h3>
            </div>
            
            <!-- Messages d'alerte -->
            <% if (success != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
              <%= success %>
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <% } %>
            
            <% if (error != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <%= error %>
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <% } %>
            
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Création de ParametreCaclulPrixType</h4>
                    <p class="card-description">Ajouter un nouveau parametre_caclul_prix_type</p>
                    <form class="forms-sample" action="saveParametreCaclulPrixType" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="reference_type_client" class="col-sm-3 col-form-label">Reference_type_client</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="reference_type_client" name="reference_type_clientId" required>
                                <option value="">Sélectionnez un Reference_type_client</option>
                                <% if (type_clients != null) {
                                    for (TypeClient type_client : type_clients) { 
%>
                                <option value="<%= type_client.getId() %>"> 
                                       
                                  <%= type_client.getNom() != null ? type_client.getNom() : "ID: " + type_client.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="object_type_client" class="col-sm-3 col-form-label">Object_type_client</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="object_type_client" name="object_type_clientId" required>
                                <option value="">Sélectionnez un Object_type_client</option>
                                <% if (type_clients != null) {
                                    for (TypeClient type_client : type_clients) { 
%>
                                <option value="<%= type_client.getId() %>"> 
                                       
                                  <%= type_client.getNom() != null ? type_client.getNom() : "ID: " + type_client.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="pourcentage" class="col-sm-3 col-form-label">Pourcentage</label>
                            <div class="col-sm-9">
                              <input type="number" class="form-control" id="pourcentage" name="pourcentage" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="signe" class="col-sm-3 col-form-label">Signe</label>
                            <div class="col-sm-9">
                              <input type="number" class="form-control" id="signe" name="signe" required>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="listeParametreCaclulPrixType" class="btn btn-dark">Annuler</a>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <%@ include file="../../includes/footer.jsp" %>
        </div>
      </div>
    </div>
    <%@ include file="../../includes/js.jsp" %>
  </body>
</html>
