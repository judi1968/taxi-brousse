<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.VoyageVoiture" %>
<%
    List<VoyageVoiture> voyageVoitures = (List<VoyageVoiture>) request.getAttribute("voyageVoitures");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des voitures par voyage</title>
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
              <h3 class="page-title">Liste des voitures par voyage</h3>
            </div>
            
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
                    <h4 class="card-title">Filtre</h4>
                    <form class="forms-sample" action="listeVoitureParVoyage" method="get">
                      <div class="row">
                        <div class="col-md-6">
                         
                          <div class="form-group row">
                            <label for="dateDiffusion" class="col-sm-3 col-form-label">Chosir mois anne</label>
                            <div class="col-sm-9">
                              <input type="month" class="form-control" id="dateDiffusion" name="moisAnne" >
                            </div>
                            <h1>            <%= (String) request.getAttribute("ca")  %>
</h1>
                          </div>
                  
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Chercher</button>
                    </form>
                  </div>
                </div>
              </div>
            </div>

            <%= (String) request.getAttribute("voyageParVoitureTab")  %>
            <%= (String) request.getAttribute("caProduitTab")  %>
            <%= (String) request.getAttribute("achatProduitTab")  %>

            
          </div>
          <%@ include file="../../includes/footer.jsp" %>
        </div>
      </div>
    </div>
    <%@ include file="../../includes/js.jsp" %>
  </body>
</html>