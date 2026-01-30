<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.DiffusionSociete" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<DiffusionSociete> diffusion_societes = (List<DiffusionSociete>) request.getAttribute("diffusion_societes");
    String error = (String) request.getAttribute("error");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des diffusion_societes</title>
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
              <h3 class="page-title">Liste des diffusion_societes</h3>
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
                    <h4 class="card-title">Liste des diffusion_societes</h4>
                    <p class="card-description">Tous les diffusion_societes disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdSociete</th>
                            <th>DateDiffusion</th>
                            <th>NombrePub</th>
                            <th>IdVoyageVoiture</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (diffusion_societes != null && !diffusion_societes.isEmpty()) {
                               for (DiffusionSociete diffusion_societe : diffusion_societes) { 
%>
                          <tr>
                            <td><%= diffusion_societe.getId() %></td>
                            <td>
                              <% if (diffusion_societe.getSociete() != null) { %>
                                <%= diffusion_societe.getSociete().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (diffusion_societe.getDateDiffusionObject() != null) { %>
                                <%= sdf.format(diffusion_societe.getDateDiffusionObject()) %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= diffusion_societe.getNombrePub() %></td>
                            <td>
                              <% if (diffusion_societe.getVoyage_voiture() != null) { %>
                                <%= diffusion_societe.getVoyage_voiture().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="5" class="text-center">Aucun diffusion_societe trouvé</td>
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
