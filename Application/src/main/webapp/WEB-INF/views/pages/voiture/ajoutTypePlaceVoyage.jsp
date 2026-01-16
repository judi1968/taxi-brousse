<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.dto.TypePlaceVoyageDTO" %>
<%@ page import="com.project.model.table.VoyageVoiture" %>
<%@ page import="com.project.model.table.PlaceVoiture" %>
<%@ page import="com.project.model.table.TypePlace" %>
<%
    List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) request.getAttribute("voyageVoitures");
    List<PlaceVoiture> places = (List<PlaceVoiture>) request.getAttribute("places");
    List<TypePlace> typePlaces = (List<TypePlace>) request.getAttribute("typePlaces");
    TypePlaceVoyageDTO typePlaceVoyageDTO = (TypePlaceVoyageDTO) request.getAttribute("typePlaceVoyageDTO");
    if (typePlaceVoyageDTO == null) {
        typePlaceVoyageDTO = new TypePlaceVoyageDTO();
    }
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Assignation Type de Place par Voyage</title>
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
              <h3 class="page-title">Assignation Type de Place par Voyage</h3>
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
                    <h4 class="card-title">Assignation Type de Place</h4>
                    <p class="card-description">Associer un type de place à une place spécifique d'un voyage</p>
                    <form class="forms-sample" action="saveTypePlaceVoyage" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <!-- VoyageVoiture -->
                          <div class="form-group row">
                            <label for="idVoyageVoiture" class="col-sm-4 col-form-label">Voyage-Voiture</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idVoyageVoiture" name="idVoyageVoiture" required>
                                <option value="">Sélectionnez un voyage-voiture</option>
                                <% if (voyageVoitures != null) {
                                    for (VoyageVoiture vv : voyageVoitures) { 
                                %>
                                <option value="<%= vv.getId() %>"
                                        <%= (typePlaceVoyageDTO.getIdVoyageVoiture() != null && typePlaceVoyageDTO.getIdVoyageVoiture().equals(vv.getId())) ? "selected" : "" %>>
                                  Voyage: <%= vv.getVoyage() != null ? vv.getVoyage().getNom() : "N/A" %> - 
                                  Voiture: <%= vv.getVoiture() != null ? vv.getVoiture().getNom() : "N/A" %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                          
                          <!-- Place -->
                          <div class="form-group row">
                            <label for="idPlace" class="col-sm-4 col-form-label">Place</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idPlace" name="idPlace" required>
                                <option value="">Sélectionnez une place</option>
                                <% if (places != null) {
                                    for (PlaceVoiture place : places) { 
                                %>
                                <option value="<%= place.getId() %>"
                                        <%= (typePlaceVoyageDTO.getIdPlace() != null && typePlaceVoyageDTO.getIdPlace().equals(place.getId())) ? "selected" : "" %>>
                                  Place <%= place.getNumero() %> - 
                                  Voiture: <%= place.getVoiture() != null ? place.getVoiture().getNom() : "N/A" %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                          
                          <!-- TypePlace -->
                          <div class="form-group row">
                            <label for="idTypePlace" class="col-sm-4 col-form-label">Type de Place</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idTypePlace" name="idTypePlace" required>
                                <option value="">Sélectionnez un type</option>
                                <% if (typePlaces != null) {
                                    for (TypePlace typePlace : typePlaces) { 
                                %>
                                <option value="<%= typePlace.getId() %>"
                                        <%= (typePlaceVoyageDTO.getIdTypePlace() != null && typePlaceVoyageDTO.getIdTypePlace().equals(typePlace.getId())) ? "selected" : "" %>>
                                  <%= typePlace.getNom() %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>               
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Assigner</button>
                      <a href="/ajoutTypePlaceVoyage" class="btn btn-dark">Annuler</a>
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