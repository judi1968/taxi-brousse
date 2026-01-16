<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypePlace" %>
<%@ page import="com.project.model.table.Voyage" %>
<%
    List<TypePlace> typePlaces = (List<TypePlace>) request.getAttribute("typePlaces");
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");

    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Ajout de Prix par Type de Place et Voyage</title>
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
              <h3 class="page-title">Ajout de Prix</h3>
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
              <button type="button" class.close data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <% } %>
            
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Définition des Prix</h4>
                    <p class="card-description">Définir le prix pour chaque type de place par voyage</p>
                    <form class="forms-sample" action="savePrixTypePlaceVoyage" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <!-- TypePlace -->
                          <div class="form-group row">
                            <label for="idTypePlace" class="col-sm-4 col-form-label">Type de Place</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idTypePlace" name="idTypePlace" required>
                                <option value="">Sélectionnez un type de place</option>
                                <% if (typePlaces != null) {
                                    for (TypePlace typePlace : typePlaces) { 
                                %>
                                <option value="<%= typePlace.getId() %>">
                                  <%= typePlace.getNom() %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                          
                          <!-- Voyage -->
                          <div class="form-group row">
                            <label for="idVoyage" class="col-sm-4 col-form-label">Voyage</label>
                            <div class="col-sm-8">
                              <select class="form-control" id="idVoyage" name="idVoyage" required>
                                <option value="">Sélectionnez un voyage</option>
                                <% if (voyages != null) {
                                    for (Voyage voyage : voyages) { 
                                %>
                                <option value="<%= voyage.getId() %>">
                                  <%= voyage.getNom() %>
                                 
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                          
                          <!-- Montant -->
                          <div class="form-group row">
                            <label for="montant" class="col-sm-4 col-form-label">Montant (€)</label>
                            <div class="col-sm-8">
                              <input type="number" class="form-control" id="montant" name="montant" 
                                     placeholder="Ex: 50.00" step="0.01" min="0" required>
                            </div>
                          </div>
                        </div>               
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer le Prix</button>
                      <a href="/ajoutPrix" class="btn btn-dark">Annuler</a>
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