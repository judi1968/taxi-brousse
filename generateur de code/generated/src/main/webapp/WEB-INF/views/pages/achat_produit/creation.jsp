<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.Produit" %>
<%
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    List<Produit> produits = (List<Produit>) request.getAttribute("produits");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <ma`et name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Creation AchatProduit</title>
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
              <h3 class="page-title">Creation AchatProduit</h3>
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
                    <h4 class="card-title">Création de AchatProduit</h4>
                    <p class="card-description">Ajouter un nouveau achat_produit</p>
                    <form class="forms-sample" action="saveAchatProduit" method="post">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="produit" class="col-sm-3 col-form-label">Produit</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="produit" name="produitId" required>
                                <option value="">Sélectionnez un Produit</option>
                                <% if (produits != null) {
                                    for (Produit produit : produits) { 
%>
                                <option value="<%= produit.getId() %>"> 
                                       
                                  <%= produit.getNom() != null ? produit.getNom() : "ID: " + produit.getId() %>
                                </option>
<% }
                                } %>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="quantite" class="col-sm-3 col-form-label">Quantite</label>
                            <div class="col-sm-9">
                              <input type="number" class="form-control" id="quantite" name="quantite" required>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="dateAchat" class="col-sm-3 col-form-label">DateAchat</label>
                            <div class="col-sm-9">
                              <input type="datetime-local" class="form-control" id="dateAchat" name="dateAchat" required>
                            </div>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Enregistrer</button>
                      <a href="listeAchatProduit" class="btn btn-dark">Annuler</a>
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
