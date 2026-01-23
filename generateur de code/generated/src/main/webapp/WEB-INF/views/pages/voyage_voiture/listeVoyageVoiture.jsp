<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.VoyageVoiture" %>
<%
    List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) request.getAttribute("voyage_voitures");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des voyage_voitures</title>
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
              <h3 class="page-title">Liste des voyage_voitures</h3>
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
                    <h4 class="card-title">Liste des voyage_voitures</h4>
                    <p class="card-description">Tous les voyage_voitures disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>IdVoiture</th>
                            <th>IdVoyage</th>
                            <th>Id</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (voyage_voitures != null && !voyage_voitures.isEmpty()) {
                               for (VoyageVoiture voyage_voiture : voyage_voitures) { 
%>
                          <tr>
                            <td>
                              <% if (voyage_voiture.getVoiture() != null) { %>
                                <%= voyage_voiture.getVoiture().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (voyage_voiture.getVoyage() != null) { %>
                                <%= voyage_voiture.getVoyage().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= voyage_voiture.getId() %></td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="3" class="text-center">Aucun voyage_voiture trouvé</td>
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
