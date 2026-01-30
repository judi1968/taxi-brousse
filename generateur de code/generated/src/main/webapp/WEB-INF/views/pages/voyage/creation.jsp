<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.Gare" %>
<%@ page import="com.project.model.table.Gare" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<Gare> gares = (List<Gare>) request.getAttribute("gares");
    List<Gare> gares = (List<Gare>) request.getAttribute("gares");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation Voyage</title>
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
              <h3 class="page-title">Creation Voyage</h3>
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
                    <h4 class="card-title">Création de Voyage</h4>
                    <p class="card-description">Ajouter un nouveau voyage</p>
                    <form class="forms-sample" action="saveVoyage" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="nom" class="col-sm-3 col-form-label">Nom</label>
                            <div class="col-sm-9">
                              <input type="text" class="form-control" id="nom" name="nom" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="date" class="col-sm-3 col-form-label">Date</label>
                            <div class="col-sm-9">
                              <input type="datetime-local" class="form-control" id="date" name="date" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="gare_depart" class="col-sm-3 col-form-label">Gare_depart</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="gare_depart" name="gare_departId" required>
                                <option value="">Sélectionnez un Gare_depart</option>
                                <% if (gares != null) {
                                    for (Gare gare : gares) { 
%>
                                <option value="<%= gare.getId() %>"> 
                                       
                                  <%= gare.getNom() != null ? gare.getNom() : "ID: " + gare.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="gare_arrive" class="col-sm-3 col-form-label">Gare_arrive</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="gare_arrive" name="gare_arriveId" required>
                                <option value="">Sélectionnez un Gare_arrive</option>
                                <% if (gares != null) {
                                    for (Gare gare : gares) { 
%>
                                <option value="<%= gare.getId() %>"> 
                                       
                                  <%= gare.getNom() != null ? gare.getNom() : "ID: " + gare.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="listeVoyage" class="btn btn-dark">Annuler</a>
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
