<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.MouvementPrixProduit" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MouvementPrixProduit> mouvement_prix_produits = (List<MouvementPrixProduit>) request.getAttribute("mouvement_prix_produits");
    String error = (String) request.getAttribute("error");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des mouvement_prix_produits</title>
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
              <h3 class="page-title">Liste des mouvement_prix_produits</h3>
            </div>
            
            <% if (error != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <%= error %>
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <% } %>
            
            <div class="row">
              <div class="col-lg-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Liste des mouvement_prix_produits</h4>
                    <p class="card-description">Tous les mouvement_prix_produits disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdProduit</th>
                            <th>Montant</th>
                            <th>DateMouvement</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (mouvement_prix_produits != null && !mouvement_prix_produits.isEmpty()) {
                               for (MouvementPrixProduit mouvement_prix_produit : mouvement_prix_produits) { 
%>
                          <tr>
                            <td><%= mouvement_prix_produit.getId() %></td>
                            <td>
                              <% if (mouvement_prix_produit.getProduit() != null) { %>
                                <%= mouvement_prix_produit.getProduit().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= mouvement_prix_produit.getMontant() %></td>
                            <td>
                              <% if (mouvement_prix_produit.getDateMouvementObject() != null) { %>
                                <%= sdf.format(mouvement_prix_produit.getDateMouvementObject()) %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="4" class="text-center">Aucun mouvement_prix_produit trouvé</td>
                          </tr>
                          <% } %>
                        </tbody>
                      </table>
                    </div>
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
