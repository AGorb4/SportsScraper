<template>
  <div style="padding: 10px">
    <h1>Props Landing</h1>
  </div>
  <div class="cardsRow">
    <div class="card text-dark bg-light mb-3 text-primary" v-for="game in games" :key="game.data">
      <div class="card-body">
        <h5 class="card-title">{{ game.name }}</h5>
      </div>
    </div>
  </div>
</template>

<script>
import games from "../assets/data/DraftkingsEvents.json";
import "primeflex/primeflex.css"; // Import the PrimeVue layout utility library.

export default {
  components: {},
  data() {
    return {
      games: games,
    };
  },
};
</script>

<style scoped>
.cardsRow {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

.stat {
  padding: 0px 20px 0px 0px;
}
</style>