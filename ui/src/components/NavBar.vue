<template>
  <div>
    <nav
      class="navbar navbar-expand-lg navbar-light"
      style="background-color: #e3f2fd; padding: 10px"
    >
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Sports Scraper</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNavAltMarkup"
          aria-controls="navbarNavAltMarkup"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
          <div class="navbar-nav">
            <router-link to="/">
              <a class="nav-link" href="">Home</a>
            </router-link>
            <router-link to="/teams">
              <a class="nav-link" href="">Teams</a>
            </router-link>
            <router-link to="/players">
              <a class="nav-link" href="">Players</a>
            </router-link>
            <router-link to="/props">
              <a class="nav-link" href="">Props</a>
            </router-link>
            <router-link to="/about">
              <a class="nav-link" href="">About</a>
            </router-link>
          </div>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  name: "NavBar",
  props: {
    msg: String,
  },
};
</script>