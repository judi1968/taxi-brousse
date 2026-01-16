<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
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
              <h3 class="page-title">Creation voyage</h3>
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
                              <input type="text" class="form-control" id="nom" name="nom" 
                      
                                     placeholder="Nom du voyage" >
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="dateStr" class="col-sm-3 col-form-label">Date</label>
                            <div class="col-sm-9">
                              <input type="datetime-local" class="form-control" id="dateStr" name="dateStr" 
                      
                                     >
                            </div>
                          </div>                   
                        </div>               
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="/voyage" class="btn btn-dark">Annuler</a>
                      <a href="listeVoyage" class="btn btn-info me-2">Voir la liste</a>
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