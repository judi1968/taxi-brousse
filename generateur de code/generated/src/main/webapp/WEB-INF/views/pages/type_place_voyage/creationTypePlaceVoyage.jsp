<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.VoyageVoiture" %>
<%@ page import="com.project.model.table.PlaceVoiture" %>
<%@ page import="com.project.model.table.TypePlace" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<VoyageVoiture> voyage_voitures = (List<VoyageVoiture>) request.getAttribute("voyage_voitures");
    List<PlaceVoiture> place_voitures = (List<PlaceVoiture>) request.getAttribute("place_voitures");
    List<TypePlace> type_places = (List<TypePlace>) request.getAttribute("type_places");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation TypePlaceVoyage</title>
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
              <h3 class="page-title">Creation TypePlaceVoyage</h3>
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
                    <h4 class="card-title">Création de TypePlaceVoyage</h4>
                    <p class="card-description">Ajouter un nouveau type_place_voyage</p>
                    <form class="forms-sample" action="saveTypePlaceVoyage" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="voyage_voiture" class="col-sm-3 col-form-label">Voyage_voiture</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="voyage_voiture" name="voyage_voiture" required>
                                <option value="">Sélectionnez un Voyage_voiture</option>
                                <% if (voyage_voitures != null) {
                                    for (VoyageVoiture voyage_voiture : voyage_voitures) { 
%>
                                <option value="<%= voyage_voiture.getId() %>"> 
                                       
                                  <%= voyage_voiture.getNom() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="place" class="col-sm-3 col-form-label">Place</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="place" name="place" required>
                                <option value="">Sélectionnez un Place</option>
                                <% if (place_voitures != null) {
                                    for (PlaceVoiture place_voiture : place_voitures) { 
%>
                                <option value="<%= place_voiture.getId() %>"> 
                                       
                                  <%= place_voiture.getNom() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="type_place" class="col-sm-3 col-form-label">Type_place</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="type_place" name="type_place" required>
                                <option value="">Sélectionnez un Type_place</option>
                                <% if (type_places != null) {
                                    for (TypePlace type_place : type_places) { 
%>
                                <option value="<%= type_place.getId() %>"> 
                                       
                                  <%= type_place.getNom() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="/type_place_voyage" class="btn btn-dark">Annuler</a>
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
