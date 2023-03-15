<template>
    <div class="container">

        <!-- Search bar -->
        <div class="row" style="padding: 10px">
            <div class="col-md-4">
                <form @submit.prevent="submitForm" class="mb-3">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="Player's Name" v-model="searchTerm">
                        <button class="btn btn-outline-secondary" type="submit" id="button-addon2">Search</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Player name heading -->
        <div class="row">
            <div class=" col-sm-4">
                <h1>Image</h1>
            </div>
            <div class=" col-sm-8">
                <h1>{{ playerName }}</h1>
            </div>
        </div>

        <!-- Top info bar -->
        <div class="row">
            <div class="col-md-3">
                <h1>Points</h1>
            </div>
            <div class="col-md-3">
                <h1>FG Share</h1>
            </div>
            <div class="col-md-3">
                <h1>Rebounds</h1>
            </div>
            <div class="col-md-3">
                <h1>Assists</h1>
            </div>
        </div>

        <!-- Graph row -->
        <div class="row">
            <div class="col">
                <h1>Graph</h1>
            </div>
        </div>

        <!-- Gamelog row -->
        <div class="row">
            <div class="col">
                <h1>Gamelog</h1>
            </div>
        </div>

    </div>
</template>

<script>
import "primeflex/primeflex.css"; // Import the PrimeVue layout utility library.

export default {
    components: {},
    data() {
        return {
            playerName: 'Joel Embiid',
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