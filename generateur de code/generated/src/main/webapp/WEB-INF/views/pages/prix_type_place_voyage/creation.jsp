<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypePlace" %>
<%@ page import="com.project.model.table.Voyage" %>
<%@ page import="com.project.model.table.TypeClient" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<TypePlace> type_places = (List<TypePlace>) request.getAttribute("type_places");
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");
    List<TypeClient> type_clients = (List<TypeClient>) request.getAttribute("type_clients");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation PrixTypePlaceVoyage</title>
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
              <h3 class="page-title">Creation PrixTypePlaceVoyage</h3>
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
                    <h4 class="card-title">Création de PrixTypePlaceVoyage</h4>
                    <p class="card-description">Ajouter un nouveau prix_type_place_voyage</p>
                    <form class="forms-sample" action="savePrixTypePlaceVoyage" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="type_place" class="col-sm-3 col-form-label">Type_place</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="type_place" name="type_placeId" required>
                                <option value="">Sélectionnez un Type_place</option>
                                <% if (type_places != null) {
                                    for (TypePlace type_place : type_places) { 
%>
                                <option value="<%= type_place.getId() %>"> 
                                       
                                  <%= type_place.getNom() != null ? type_place.getNom() : "ID: " + type_place.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="voyage" class="col-sm-3 col-form-label">Voyage</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="voyage" name="voyageId" required>
                                <option value="">Sélectionnez un Voyage</option>
                                <% if (voyages != null) {
                                    for (Voyage voyage : voyages) { 
%>
                                <option value="<%= voyage.getId() %>"> 
                                       
                                  <%= voyage.getNom() != null ? voyage.getNom() : "ID: " + voyage.getId() %>
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
                            <label for="type_client" class="col-sm-3 col-form-label">Type_client</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="type_client" name="type_clientId" required>
                                <option value="">Sélectionnez un Type_client</option>
                                <% if (type_clients != null) {
                                    for (TypeClient type_client : type_clients) { 
%>
                                <option value="<%= type_client.getId() %>"> 
                                       
                                  <%= type_client.getNom() != null ? type_client.getNom() : "ID: " + type_client.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="listePrixTypePlaceVoyage" class="btn btn-dark">Annuler</a>
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
