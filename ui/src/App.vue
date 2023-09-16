<template>
  <div class="container">
    <NavBar />
    <router-view />
    <!--  <Footer /> -->
  </div>
</template>

<style  lang="scss">
@import "~primevue/resources/primevue.min.css";
@import "~primevue/resources/themes/bootstrap4-light-blue/theme.css";

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

<script>
import NavBar from "@/components/NavBar.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: "App",
  components: {
    NavBar,
    Footer
  },
};
</script>

