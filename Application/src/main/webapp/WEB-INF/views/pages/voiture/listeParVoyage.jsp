<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.VoyageVoiture" %>
<%
    List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) request.getAttribute("voyageVoitures");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des voitures par voyage</title>
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
              <h3 class="page-title">Liste des voitures par voyage</h3>
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
                    <h4 class="card-title">Liste des associations Voyage-Voiture</h4>
                    <p class="card-description">Voitures assignées à chaque voyage</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>ID</th>
                            <th>Voyage</th>
                            <th>Date Voyage</th>
                            <th>Voiture</th>
                            <th>Numéro Voiture</th>
                            <th>Prix (Ar)</th>
                            <th>CA (Ar)</th>
                            <th>Actions</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (voyageVoitures != null && !voyageVoitures.isEmpty()) {
                               for (VoyageVoiture vv : voyageVoitures) { 
                          %>
                          <tr>
                            <td><%= vv.getId() %></td>
                            <td>
                              <% if (vv.getVoyage() != null) { %>
                                <%= vv.getVoyage().getNom() %>
                              <% } else { %>
                                N/A
                              <% } %>
                            </td>
                            <td>
                              <% if (vv.getVoyage() != null && vv.getVoyage().getDate() != null) { %>
                                <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(vv.getVoyage().getDate()) %>
                              <% } else { %>
                                N/A
                              <% } %>
                            </td>
                            <td>
                              <% if (vv.getVoiture() != null) { %>
                                <%= vv.getVoiture().getNom() %>
                              <% } else { %>
                                N/A
                              <% } %>
                            </td>
                            <td>
                              <% if (vv.getVoiture() != null) { %>
                                <%= vv.getVoiture().getNumero() %>
                              <% } else { %>
                                N/A
                              <% } %>
                            </td>
                            <td class="text-success"> <%= vv.getPrixMaximum() %></td>
                            <td class="text-primary"> <%= vv.getMontantCA() %></td>
                            <td>
                              <a href="see" class="btn btn-info btn-sm">
                                <i class="mdi mdi-eye"></i> Voir
                              </a>
                              <a href="modify" class="btn btn-primary btn-sm">
                                <i class="mdi mdi-pen"></i> Modifier
                              </a>
                              <a href="delete" class="btn btn-danger btn-sm">
                                <i class="mdi mdi-delete"></i> Supprimer
                              </a>
                            </td>
                          </tr>
                          <% }
                             } else { %>
                          <tr>
                            <td colspan="7" class="text-center">Aucune association voyage-voiture trouvée</td>
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