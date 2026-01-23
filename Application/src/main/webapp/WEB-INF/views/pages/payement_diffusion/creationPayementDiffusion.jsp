<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.Societe" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<Societe> societes = (List<Societe>) request.getAttribute("societes");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
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
                    <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="/payement_diffusion" class="btn btn-dark">Annuler</a>
                      <div class="row">
                        <div class="col-md-6">
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
                          <div class="form-group row">
                            <label for="societe" class="col-sm-3 col-form-label">Societe</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="societe" name="societeId" required>
                                <option value="">Sélectionnez un Societe  </option>
                                <% if (societes != null) {
                                    for (Societe societe : societes) { 
%>  
                                <option value="<%= societe.getId() %>"> 
                                       
                                   <%= societe.getNom() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          
                      
                      </div>
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
