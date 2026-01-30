<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.Societe" %>
<%@ page import="com.project.model.table.VoyageVoiture" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<Societe> societes = (List<Societe>) request.getAttribute("societes");
    List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) request.getAttribute("voyage_voitures");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation DiffusionSociete</title>
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
              <h3 class="page-title">Creation DiffusionSociete</h3>
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
                    <h4 class="card-title">Création de DiffusionSociete</h4>
                    <p class="card-description">Ajouter un nouveau diffusion_societe</p>
                    <form class="forms-sample" action="saveDiffusionSociete" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="societe" class="col-sm-3 col-form-label">Societe</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="societe" name="societeId" required>
                                <option value="">Sélectionnez un Societe</option>
                                <% if (societes != null) {
                                    for (Societe societe : societes) { 
%>
                                <option value="<%= societe.getId() %>"> 
                                       
                                  <%= societe.getNom() != null ? societe.getNom() : "ID: " + societe.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="dateDiffusion" class="col-sm-3 col-form-label">DateDiffusion</label>
                            <div class="col-sm-9">
                              <input type="datetime-local" class="form-control" id="dateDiffusion" name="dateDiffusion" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="nombrePub" class="col-sm-3 col-form-label">NombrePub</label>
                            <div class="col-sm-9">
                              <input type="number" class="form-control" id="nombrePub" name="nombrePub" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="voyage_voiture" class="col-sm-3 col-form-label">Voyage_voiture</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="voyage_voiture" name="voyage_voitureId" required>
                                <option value="">Sélectionnez un Voyage_voiture</option>
                                <% if (voyage_voitures != null) {
                                    for (VoyageVoiture voyage_voiture : voyage_voitures) { 
%>
                                <option value="<%= voyage_voiture.getId() %>"> 
                                       
                                  <%= voyage_voiture.getNom() != null ? voyage_voiture.getNom() : "ID: " + voyage_voiture.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="listeDiffusionSociete" class="btn btn-dark">Annuler</a>
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
