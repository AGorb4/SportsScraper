<template>
  <div style="padding: 10px">
    <h1>Props Analyzer</h1>
  </div>
  <div class="row p-3">
    <div class="col">
      <form @submit.prevent="submitForm" class="mb-3">
        <div class="mb-3"><input v-model="playerName" placeholder="Player Name" /></div>


        <select class="mb-3" v-model="propType">
          <option value="points">Points</option>
          <option value="threes">Threes</option>
          <option value="rebounds">Rebounds</option>
          <option value="assists">Assists</option>
          <option value="stl">Steals</option>
          <option value="blk">Blocks</option>
          <option value="to">Turnovers</option>
          <option value="ptsRebAst">Pts + Rbs + Asts</option>
          <option value="ptsReb">Pts + Rbs</option>
          <option value="ptsAst">Pts + Asts</option>
          <option value="astsReb">Rbs + Asts</option>
          <option value="stlsBlk">Steals + Blocks</option>
        </select>

        <div class="mb-3"><input v-model="propTotal" placeholder="Prop Total" /></div>
        <div class="mb-3"><input v-model="lastN" placeholder="Last # of Games" /></div>
        <button type="submit" class="btn btn-primary">Submit</button>
      </form>
    </div>
    <div class="cardsRow col-md-6">
      <div class="card col-md-8 text-dark bg-light mb-3 text-primary border-dark" v-for="(prop, index) in propsHistory"
        :key="prop.data">
        <div class="row">
          <div class="col-md-3 p-3">
            <img :src=prop.playerPictureUrl alt="Player picture" style="max-height: 75%; max-width: 50%;">
            <h5 class="card-title p-1">{{ prop.playerName }}</h5>
          </div>
          <div class="card-body col-md-9 mt-4">
            <h5>{{ prop.propTotal }} {{ prop.propType }}</h5>
            <h5>{{ prop.lastNRecord }}</h5>
            <h5><strong>{{ prop.winPercentage }}%</strong></h5>
            <button class="btn btn-danger" v-on:click="test(index)"><i class="fa fa-trash"></i></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  components: {},
  data() {
    return {
      propResults: null,
      playerName: '',
      propType: '',
      propTotal: null,
      lastN: 0,
      propsHistory: []
    };
  },
  methods: {
    submitForm() {
      console.log("Called submit form")
      console.log(this.playerName)
      console.log(this.propType)
      console.log(this.propTotal)
      console.log(this.lastN)
      let response = axios.get('http://localhost:8090/props/NBA/statistics/2023/' + this.propType + '/' + this.propTotal + '/' + this.playerName + '/' + this.lastN)
      response.then((result) => {
        console.log(result.data)
        this.propsHistory.unshift({
          "playerName": this.playerName,
          "propType": this.propType,
          "propTotal": this.propTotal,
          "lastN": this.lastN,
          "playerPictureUrl": result.data.playerPictureUrl,
          "playerAverage": result.data.playerAverage,
          "lastNRecord": result.data.lastNRecord,
          "winPercentage": result.data.lastNWinPercentage,
          "id": this.propsHistory.length + 1
        })
      })
      console.log(this.propsHistory)
    },
    test(index) {
      console.log("index " + index)
      console.log(this.propsHistory)
      this.propsHistory.splice(index, 1)
    }
  },
};
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css");

.cardsRow {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}

.stat {
  padding: 0px 20px 0px 0px;
}
</style>