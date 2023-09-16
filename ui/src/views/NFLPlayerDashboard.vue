<template>
    <div class="dashboard">
        <h2>NFL Player Prop Dashboard</h2>
        <form @submit.prevent="searchProps" class="prop-search-form">
            <div class="form-group">
                <label for="playerName">Enter Player Name:</label>
                <input type="text" id="playerName" v-model="searchTerm">
            </div>
            <div class="form-group">
                <label for="propType">Select Prop Type:</label>
                <select id="propType" v-model="selectedPropType">
                    <option value="passingYards">Passing Yards</option>
                    <option value="passingTouchdowns">Passing Touchdowns</option>
                    <option value="rushingYards">Rushing Yards</option>
                    <option value="rushingTouchdowns">Rushing Touchdowns</option>
                    <option value="receivingYards">Receiving Yards</option>
                    <option value="receivingTouchdowns">Receiving Touchdowns</option>
                </select>
            </div>
            <div class="form-group">
                <label for="comparisonValue">Enter Comparison Value:</label>
                <input type="number" id="comparisonValue" v-model="comparisonValue">
            </div>
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
        <div v-if="propHits.length > 0" class="prop-hits">
            <h3>Prop Hits</h3>
            <ul>
                <li v-for="hit in propHits" :key="hit.gameId">{{ hit.player }} - {{ hit.prop }} ({{ hit.date }})</li>
            </ul>
        </div>
        <div v-else class="no-hits">
            No prop hits found for the entered player prop.
        </div>
        <div v-if="gamelog.length > 0" class="gamelog-table">
            <h3>Player Gamelog</h3>
            <table>
                <thead>
                    <tr>
                        <th>Game Date</th>
                        <th>Opponent</th>
                        <th>Targets</th>
                        <th>Yards</th>
                        <th>Touchdowns</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="game in gamelog" :key="game.gameId">
                        <td>{{ game.date }}</td>
                        <td>{{ game.opponent }}</td>
                        <td>{{ game.targets }}</td>
                        <td>{{ game.yards }}</td>
                        <td>{{ game.touchdowns }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import Chart from 'chart.js';

export default {
    data() {
        return {
            selectedPropType: '',
            searchTerm: '',
            comparisonValue: null,
            propHits: [],
            gamelog: [],
            chart: null,
        };
    },
    methods: {
        submitProp() {
            // Here you would implement the logic to fetch the prop hits data and gamelog from your data source or API
            // Replace the code below with your actual API call or data retrieval logic

            // Mock data for demonstration purposes
            const mockPropHits = [
                { gameId: 1, player: 'Player A', prop: 'Touchdown Passes', date: '2023-01-01' },
                { gameId: 2, player: 'Player A', prop: 'Rushing Yards', date: '2023-01-08' },
                { gameId: 3, player: 'Player A', prop: 'Receiving Touchdowns', date: '2023-01-15' },
                { gameId: 4, player: 'Player A', prop: 'Passing Yards', date: '2023-01-22' },
            ];

            const mockGamelog = [
                { gameId: 1, date: '2023-01-01', opponent: 'Team X', targets: 8, yards: 80, touchdowns: 1 },
                { gameId: 2, date: '2023-01-08', opponent: 'Team Y', targets: 5, yards: 50, touchdowns: 0 },
                { gameId: 3, date: '2023-01-15', opponent: 'Team Z', targets: 10, yards: 120, touchdowns: 2 },
                { gameId: 4, date: '2023-01-22', opponent: 'Team W', targets: 6, yards: 70, touchdowns: 0 },
            ];

            // Filter prop hits based on the entered prop
            this.propHits = mockPropHits.filter(hit => hit.prop.toLowerCase().includes(this.prop.toLowerCase()));

            // Set the gamelog based on the player's prop hits
            this.gamelog = mockGamelog.filter(game => this.propHits.some(hit => hit.gameId === game.gameId));
        },
    },
};
</script>

<style scoped>
h2 {
    margin: 20px;
}

label {
    margin-right: 5px;
    font-weight: bold;
}

.prop-hits {
    margin-top: 10px;
}

.no-hits {
    margin-top: 10px;
}

.form-group {
    margin-bottom: 10px;
}

.btn {
    padding: 8px 16px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    max-width: 100px;
    align-self: center;
}

.btn-primary {
    background-color: #007bff;
}

.btn-primary:hover {
    background-color: #0056b3;
}

.prop-search-form {
    display: flex;
    flex-direction: column;
}
</style>
