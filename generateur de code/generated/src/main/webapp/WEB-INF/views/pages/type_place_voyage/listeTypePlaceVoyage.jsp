<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypePlaceVoyage" %>
<%
    List<TypePlaceVoyage> type_place_voyages = (List<TypePlaceVoyage>) request.getAttribute("type_place_voyages");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des type_place_voyages</title>
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
              <h3 class="page-title">Liste des type_place_voyages</h3>
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
                    <h4 class="card-title">Liste des type_place_voyages</h4>
                    <p class="card-description">Tous les type_place_voyages disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdVoyageVoiture</th>
                            <th>IdPlace</th>
                            <th>IdTypePlace</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (type_place_voyages != null && !type_place_voyages.isEmpty()) {
                               for (TypePlaceVoyage type_place_voyage : type_place_voyages) { 
%>
                          <tr>
                            <td><%= type_place_voyage.getId() %></td>
                            <td>
                              <% if (type_place_voyage.getVoyage_voiture() != null) { %>
                                <%= type_place_voyage.getVoyage_voiture().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (type_place_voyage.getPlace() != null) { %>
                                <%= type_place_voyage.getPlace().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (type_place_voyage.getType_place() != null) { %>
                                <%= type_place_voyage.getType_place().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="4" class="text-center">Aucun type_place_voyage trouvé</td>
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
