<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.DiffusionSociete" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<DiffusionSociete> diffusion_societes = (List<DiffusionSociete>) request.getAttribute("diffusion_societes");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation PayementDiffusion</title>
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
              <h3 class="page-title">Creation PayementDiffusion</h3>
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
                    <h4 class="card-title">Création de PayementDiffusion</h4>
                    <p class="card-description">Ajouter un nouveau payement_diffusion</p>
                    <form class="forms-sample" action="savePayementDiffusion" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="societe_diffusion" class="col-sm-3 col-form-label">Societe_diffusion</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="societe_diffusion" name="societe_diffusionId" required>
                                <option value="">Sélectionnez un Societe_diffusion</option>
                                <% if (diffusion_societes != null) {
                                    for (DiffusionSociete diffusion_societe : diffusion_societes) { 
%>
                                <option value="<%= diffusion_societe.getId() %>"> 
                                       
                                  <%= diffusion_societe.getNom() != null ? diffusion_societe.getNom() : "ID: " + diffusion_societe.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="montant" class="col-sm-3 col-form-label">Montant</label>
                            <div class="col-sm-9">
                              <input type="number" class="form-control" id="montant" name="montant" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="datePayement" class="col-sm-3 col-form-label">DatePayement</label>
                            <div class="col-sm-9">
                              <input type="datetime-local" class="form-control" id="datePayement" name="datePayement" required>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="listePayementDiffusion" class="btn btn-dark">Annuler</a>
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
