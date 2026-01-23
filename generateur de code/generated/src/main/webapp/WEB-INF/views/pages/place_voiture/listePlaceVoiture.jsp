<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.PlaceVoiture" %>
<%
    List<PlaceVoiture> place_voitures = (List<PlaceVoiture>) request.getAttribute("place_voitures");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des place_voitures</title>
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
              <h3 class="page-title">Liste des place_voitures</h3>
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
                    <h4 class="card-title">Liste des place_voitures</h4>
                    <p class="card-description">Tous les place_voitures disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdVoiture</th>
                            <th>Numero</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (place_voitures != null && !place_voitures.isEmpty()) {
                               for (PlaceVoiture place_voiture : place_voitures) { 
%>
                          <tr>
                            <td><%= place_voiture.getId() %></td>
                            <td>
                              <% if (place_voiture.getVoiture() != null) { %>
                                <%= place_voiture.getVoiture().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= place_voiture.getNumero() %></td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="3" class="text-center">Aucun place_voiture trouvé</td>
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
