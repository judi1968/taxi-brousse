<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypeClient" %>
<%@ page import="com.project.model.table.TypePlaceVoyage" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<TypeClient> type_clients = (List<TypeClient>) request.getAttribute("type_clients");
    List<TypePlaceVoyage> type_place_voyages = (List<TypePlaceVoyage>) request.getAttribute("type_place_voyages");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation AchatClient</title>
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
              <h3 class="page-title">Creation AchatClient</h3>
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
                    <h4 class="card-title">Création de AchatClient</h4>
                    <p class="card-description">Ajouter un nouveau achat_client</p>
                    <form class="forms-sample" action="saveAchatClient" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="type_client" class="col-sm-3 col-form-label">Type_client</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="type_client" name="type_client" required>
                                <option value="">Sélectionnez un Type_client</option>
                                <% if (type_clients != null) {
                                    for (TypeClient type_client : type_clients) { 
%>
                                <option value="<%= type_client.getId() %>"> 
                                       
                                  <%= type_client.getNom() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="type_place_voyage" class="col-sm-3 col-form-label">Type_place_voyage</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="type_place_voyage" name="type_place_voyage" required>
                                <option value="">Sélectionnez un Type_place_voyage</option>
                                <% if (type_place_voyages != null) {
                                    for (TypePlaceVoyage type_place_voyage : type_place_voyages) { 
%>
                                <option value="<%= type_place_voyage.getId() %>"> 
                                       
                                  <%= type_place_voyage.getNom() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="/achat_client" class="btn btn-dark">Annuler</a>
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
