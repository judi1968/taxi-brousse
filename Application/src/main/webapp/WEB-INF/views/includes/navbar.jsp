<nav class="sidebar sidebar-offcanvas" id="sidebar">
        <div class="sidebar-brand-wrapper d-none d-lg-flex align-items-center justify-content-center fixed-top">
          <a class="sidebar-brand brand-logo" href="index.html"><img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="logo" /></a>
          <a class="sidebar-brand brand-logo-mini" href="index.html"><img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="logo" /></a>
        </div>
        <ul class="nav">
          <li class="nav-item profile">
            <div class="profile-desc">
              <div class="profile-pic">
                <div class="count-indicator">
                  <img class="img-xs rounded-circle " src="${pageContext.request.contextPath}/assets/images/user.png" alt="">
                  <span class="count bg-success"></span>
                </div>
                <div class="profile-name">
                  <h5 class="mb-0 font-weight-normal">Henry Klein</h5>
                  <span>Gold Member</span>
                </div>
              </div>
              <a href="#" id="profile-dropdown" data-bs-toggle="dropdown"><i class="mdi mdi-dots-vertical"></i></a>
              <div class="dropdown-menu dropdown-menu-right sidebar-dropdown preview-list" aria-labelledby="profile-dropdown">
                <a href="#" class="dropdown-item preview-item">
                  <div class="preview-thumbnail">
                    <div class="preview-icon bg-dark rounded-circle">
                      <i class="mdi mdi-settings text-primary"></i>
                    </div>
                  </div>
                  <div class="preview-item-content">
                    <p class="preview-subject ellipsis mb-1 text-small">Account settings</p>
                  </div>
                </a>
                <div class="dropdown-divider"></div>
                <a href="#" class="dropdown-item preview-item">
                  <div class="preview-thumbnail">
                    <div class="preview-icon bg-dark rounded-circle">
                      <i class="mdi mdi-onepassword  text-info"></i>
                    </div>
                  </div>
                  <div class="preview-item-content">
                    <p class="preview-subject ellipsis mb-1 text-small">Change Password</p>
                  </div>
                </a>
                <div class="dropdown-divider"></div>
                <a href="#" class="dropdown-item preview-item">
                  <div class="preview-thumbnail">
                    <div class="preview-icon bg-dark rounded-circle">
                      <i class="mdi mdi-calendar-today text-success"></i>
                    </div>
                  </div>
                  <div class="preview-item-content">
                    <p class="preview-subject ellipsis mb-1 text-small">To-do list</p>
                  </div>
                </a>
              </div>
            </div>
          </li>
          <li class="nav-item nav-category">
            <span class="nav-link">Navigation</span>
          </li>
          <li class="nav-item menu-items">
  <a class="nav-link" href="home">
    <span class="menu-icon">
      <i class="mdi mdi-speedometer"></i>
    </span>
    <span class="menu-title">Dashboard</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="/">
    <span class="menu-icon">
      <i class="mdi mdi-car"></i>
    </span>
    <span class="menu-title">Creation Voiture</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="place">
    <span class="menu-icon">
      <i class="mdi mdi-seat"></i>
    </span>
    <span class="menu-title">Creation Place</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="voyage">
    <span class="menu-icon">
      <i class="mdi mdi-road-variant"></i>
    </span>
    <span class="menu-title">Creation Voyage</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="creertypePlace">
    <span class="menu-icon">
      <i class="mdi mdi-road-variant"></i>
    </span>
    <span class="menu-title">Creation type place</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="VoitureVoyage">
    <span class="menu-icon">
      <i class="mdi mdi-link-variant"></i>
    </span>
    <span class="menu-title">Association Voiture-Voyage</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" data-bs-toggle="collapse" href="#ui-liste" aria-expanded="false" aria-controls="ui-liste">
    <span class="menu-icon">
      <i class="mdi mdi-format-list-bulleted"></i>
    </span>
    <span class="menu-title">Listes</span>
    <i class="menu-arrow"></i>
  </a>
  <div class="collapse" id="ui-liste">
    <ul class="nav flex-column sub-menu">
      <li class="nav-item"> <a class="nav-link" href="listeVoitureParVoyage">Voitures par Voyage</a></li>
      <li class="nav-item"> <a class="nav-link" href="listeVoyage">Liste Voyages</a></li>
    </ul>
  </div>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="ajoutTypePlaceVoyage">
    <span class="menu-icon">
      <i class="mdi mdi-tag"></i>
    </span>
    <span class="menu-title">Type de Place</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="typeClient">
    <span class="menu-icon">
      <i class="mdi mdi-user"></i>
    </span>
    <span class="menu-title">Type de client</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="achatClient">
    <span class="menu-icon">
      <i class="mdi mdi-user"></i>
    </span>
    <span class="menu-title">achat de client</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="ajoutPrix">
    <span class="menu-icon">
      <i class="mdi mdi-cash"></i>
    </span>
    <span class="menu-title">Ajout Prix</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="form">
    <span class="menu-icon">
      <i class="mdi mdi-playlist-play"></i>
    </span>
    <span class="menu-title">Formulaire</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="table">
    <span class="menu-icon">
      <i class="mdi mdi-table-large"></i>
    </span>
    <span class="menu-title">Tables</span>
  </a>
</li>
<li class="nav-item menu-items">
  <a class="nav-link" href="log-out">
    <span class="menu-icon">
      <i class="mdi mdi-logout"></i>
    </span>
    <span class="menu-title">Deconnexion</span>
  </a>
</li>
        </ul>
      </nav>