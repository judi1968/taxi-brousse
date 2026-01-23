<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.PrixTypePlaceVoyage" %>
<%
    List<PrixTypePlaceVoyage> prix_type_place_voyages = (List<PrixTypePlaceVoyage>) request.getAttribute("prix_type_place_voyages");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des prix_type_place_voyages</title>
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
              <h3 class="page-title">Liste des prix_type_place_voyages</h3>
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
                    <h4 class="card-title">Liste des prix_type_place_voyages</h4>
                    <p class="card-description">Tous les prix_type_place_voyages disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdTypePlace</th>
                            <th>IdVoyage</th>
                            <th>Montant</th>
                            <th>IdTypeClient</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (prix_type_place_voyages != null && !prix_type_place_voyages.isEmpty()) {
                               for (PrixTypePlaceVoyage prix_type_place_voyage : prix_type_place_voyages) { 
%>
                          <tr>
                            <td><%= prix_type_place_voyage.getId() %></td>
                            <td>
                              <% if (prix_type_place_voyage.getType_place() != null) { %>
                                <%= prix_type_place_voyage.getType_place().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (prix_type_place_voyage.getVoyage() != null) { %>
                                <%= prix_type_place_voyage.getVoyage().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= prix_type_place_voyage.getMontant() %></td>
                            <td>
                              <% if (prix_type_place_voyage.getType_client() != null) { %>
                                <%= prix_type_place_voyage.getType_client().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="5" class="text-center">Aucun prix_type_place_voyage trouvé</td>
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
