<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light fixed-top" style="background-color: #e3f2fd; padding: 10px">
      <div class="container-fluid">
        <img alt="Site logo" src="../assets/NavBarLogo.png"
          style="height: auto; width: auto; max-height: 40px; max-width: 350px; padding-right: 10px;" />
        <a class="navbar-brand" href="/">Wager Insight</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
          aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNavAltMarkup">
          <div class="navbar-nav">
            <router-link to="/">
              <a class="nav-link" href="">Home</a>
            </router-link>
            <router-link to="/dashboard">
              <a class="nav-link" href="">Dashboard</a>
            </router-link>
            <router-link to="/teams">
              <a class="nav-link" href="">Resources</a>
            </router-link>
            <router-link to="/about">
              <a class="nav-link" href="">About</a>
            </router-link>
            <router-link to="/about">
              <a class="nav-link" href="">Contact Us</a>
            </router-link>
            <router-link to="/about">
              <a class="nav-link" href="">Promo Links</a>
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
          </div>
        </div>
        <!-- <form @submit.prevent="submitForm" class="mb-3">
          <div class="input-group">
            <div class="mx-2">
              <select class="form-select" aria-label="Default select example" v-model="league">
                <option selected>League</option>
                <option value="NFL">NFL</option>
                <option value="NBA">NBA</option>
              </select>
            </div>

            <input type="text" class="form-control" placeholder="Player's Name" v-model="searchTerm">
            <button class="btn btn-outline-secondary" type="submit" id="button-addon2">Search</button>
          </div>
        </form> -->
      </div>


    </nav>
  </div>
  <ul v-if="(searchList.length > 0)"
    class="w-full rounded bg-white border border-gray-300 px-4 py-2 space-y-1 absolute z-10">
    <li class="px-1 pt-1 pb-2 font-bold border-b border-gray-200">
      Showing {{ searchList.length }} of {{ searchList.length }} results
    </li>
    <li v-for="player in searchList" :key="player.name" @click="selectPlayer(player)"
      class="cursor-pointer hover:bg-gray-100 p-1">
      {{ player.name }}
    </li>
  </ul>
</template>

<script>
import axios from 'axios'

export default {
  name: "NavBar",
  components: {},
  mounted() {
    let response = axios.get('http://localhost:8090/nfl/players/2022')
    response.then((result) => {
      console.log('Got nfl players');
      this.nflPlayers = result.data;
    })

  },
  data() {
    return {
      league: "",
      searchTerm: "",
      nflPlayers: [],
      nbaPlayers: [],
      playerList: [],
      searchList: []
    };
  },
  computed: {
    searchList() {
      if (this.searchTerm === '' || this.searchTerm === ' ') {
        return []
      }
      let matches = 0
      return this.playerList.filter(player => {
        if (player.name.toLowerCase().includes(this.searchTerm.toLowerCase()) && matches < 10) {
          matches++
          return player
        }
      });
    }
  },
  methods: {
    submitForm() {
      console.log(this.league)
    },
    selectPlayer(value) {
      console.log('Selected player ' + value.name)
      this.searchTerm = ''
    }
  },
  watch: {
    league: function (value) {
      console.log('Selected league ' + value);
      if (value === 'NFL') {
        this.playerList = this.nflPlayers;
      }
    },
    searchTerm: function (value) {

    }
  }
};
</script>