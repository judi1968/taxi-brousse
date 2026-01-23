<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.PayementDiffusion" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<PayementDiffusion> payement_diffusions = (List<PayementDiffusion>) request.getAttribute("payement_diffusions");
    String error = (String) request.getAttribute("error");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des payement_diffusions</title>
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
              <h3 class="page-title">Liste des payement_diffusions</h3>
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
                    <h4 class="card-title">Liste des payement_diffusions</h4>
                    <p class="card-description">Tous les payement_diffusions disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdSocieteDiffusion</th>
                            <th>Montant</th>
                            <th>DatePayement</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (payement_diffusions != null && !payement_diffusions.isEmpty()) {
                               for (PayementDiffusion payement_diffusion : payement_diffusions) { 
%>
                          <tr>
                            <td><%= payement_diffusion.getId() %></td>
                            <td>
                              <% if (payement_diffusion.getSociete_diffusion() != null) { %>
                                <%= payement_diffusion.getSociete_diffusion().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= payement_diffusion.getMontant() %></td>
                            <td>
                              <% if (payement_diffusion.getDatePayementObject() != null) { %>
                                <%= sdf.format(payement_diffusion.getDatePayementObject()) %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="4" class="text-center">Aucun payement_diffusion trouvé</td>
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
