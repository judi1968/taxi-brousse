<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypeClient" %>
<%@ page import="com.project.model.table.TypePlaceVoyage" %>
<%
    List<TypeClient> typeClients = (List<TypeClient>) request.getAttribute("typeClients");
    List<TypePlaceVoyage> typePlaceVoyages = (List<TypePlaceVoyage>) request.getAttribute("typePlaceVoyages");
    
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Achat Client</title>
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
              <h3 class="page-title">Enregistrement Achat Client</h3>
            </div>
            
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
                    <h4 class="card-title">Enregistrement d'un Achat</h4>
                    <p class="card-description">Associer un type de client à une réservation de place</p>
                    <form class="forms-sample" action="saveAchatClient" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <!-- Type Client -->
                          <div class="form-group row">
                            <label for="idTypeClient" class="col-sm-4 col-form-label">Type de Client</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idTypeClient" name="idTypeClient" required>
                                <option value="">Sélectionnez un type de client</option>
                                <% if (typeClients != null) {
                                    for (TypeClient typeClient : typeClients) { 
                                %>
                                <option value="<%= typeClient.getId() %>">
                                  <%= typeClient.getNom() %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                          
                          <!-- Type Place Voyage -->
                          <div class="form-group row">
                            <label for="idTypePlaceVoyage" class="col-sm-4 col-form-label">Réservation</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idTypePlaceVoyage" name="idTypePlaceVoyage" required>
                                <option value="">Sélectionnez une réservation</option>
                                <% if (typePlaceVoyages != null) {
                                    for (TypePlaceVoyage tpv : typePlaceVoyages) { 
                                %>
                                <option value="<%= tpv.getId() %>">
                                  <% if (tpv.getVoyageVoiture() != null && tpv.getVoyageVoiture().getVoyage() != null) { %>
                                    Voyage: <%= tpv.getVoyageVoiture().getVoyage().getNom() %>
                                  <% } %>
                                  <% if (tpv.getPlace() != null) { %>
                                    - Place: <%= tpv.getPlace().getNumero() %>
                                  <% } %>
                                  <% if (tpv.getTypePlace() != null) { %>
                                    (<%= tpv.getTypePlace().getNom() %>)
                                  <% } %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>               
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer l'Achat</button>
                      <a href="/achatClient" class="btn btn-dark">Annuler</a>
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