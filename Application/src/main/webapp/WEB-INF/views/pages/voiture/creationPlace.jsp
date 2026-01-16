<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.Voiture" %>
<%
    List<Voiture> voitures = (List<Voiture>) request.getAttribute("voitures");
    
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation Place des voiture</title>
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
              <h3 class="page-title">Creation Place des voiture</h3>
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
                    <h4 class="card-title">Création de Place</h4>
                    <p class="card-description">Ajouter une nouvelle place à une voiture</p>
                    <form class="forms-sample" action="savePlaceVoiture" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="idVoiture" class="col-sm-3 col-form-label">Voiture</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="idVoiture" name="idVoiture" required>
                                <option value="">Sélectionnez une voiture</option>
                                <% if (voitures != null) {
                                    for (Voiture voiture : voitures) { 
                                %>
                                <option value="<%= voiture.getId() %>"> 
                                       
                                  <%= voiture.getNom() %> (<%= voiture.getNumero() %>
                                </option>
                                <% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="numero" class="col-sm-3 col-form-label">Numéro Place</label>
                            <div class="col-sm-9">
                              <input type="text" class="form-control" id="numero" name="numero" 
                                     placeholder="Ex: 1, 2, 3..." required>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="/place" class="btn btn-dark">Annuler</a>
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