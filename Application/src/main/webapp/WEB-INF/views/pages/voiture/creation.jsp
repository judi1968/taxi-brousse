<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation Voiture</title>
    <!-- plugins:css -->
    <%@ include file="../../includes/css.jsp" %>
  </head>
  <body>
    <div class="container-scroller">
     
      <!-- partial:partials/_sidebar.html -->
      
      <%@ include file="../../includes/navbar.jsp" %>
      <!-- partial -->
      <div class="container-fluid page-body-wrapper">
        <!-- partial:partials/_navbar.html -->
        <%@ include file="../../includes/header.jsp" %>
        <!-- partial -->
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="page-header">
              <h3 class="page-title"> Creation voiture</h3>

            </div>
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Horizontal Form</h4>
                    <p class="card-description"> Horizontal form layout </p>
                    <form class="forms-sample" action="saveVoiture" method="post">
                      <div class="row">
                          <div class="col-md-6">
                              <div class="form-group row">
                                  <label for="nom" class="col-sm-3 col-form-label">Nom</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="nom" name="nom" placeholder="Nom de la voiture">
                                  </div>
                              </div>
                              <div class="form-group row">
                                  <label for="numero" class="col-sm-3 col-form-label">Numéro</label>
                                  <div class="col-sm-9">
                                      <input type="text" class="form-control" id="numero" name="numero" placeholder="Numéro d'immatriculation">
                                  </div>
                              </div>                   
                          </div>               
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="/" class="btn btn-dark">Annuler</a>
                  </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- content-wrapper ends -->
          <!-- partial:../../partials/_footer.html -->
          
          <%@ include file="../../includes/footer.jsp" %>
          <!-- partial -->
        </div>
        <!-- main-panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
    <!-- container-scroller -->
    <%@ include file="../../includes/js.jsp" %>
  </body>
</html>