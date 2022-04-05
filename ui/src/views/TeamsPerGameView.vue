<template>
  <div style="padding: 10px">
    <h1>Teams Per Game Stats</h1>
  </div>
  <div class="cardsRow" v-if="selected != null && selected.length > 0">
    <div
      class="card text-dark bg-light mb-3 text-primary"
      v-for="team in selected"
      :key="team.data"
    >
      <div class="card-body">
        <h5 class="card-title">{{ team.teamName }}</h5>
        <hr />
        <h6
          class="card-subtitle mb-2 text-muted stat"
          style="padding-bottom: 10px"
        >
          <b>PTS -</b> {{ team.points }}
        </h6>
        <div class="cardsRow">
          <p class="text-muted text-left stat">
            <b>FG -</b> {{ team.fieldGoals }}
          </p>
          <p class="text-muted text-left stat">
            <b>FGA -</b> {{ team.fieldGoalAttempts }}
          </p>
          <p class="text-muted text-left stat">
            <b>FG% -</b> {{ team.fieldGoalPercentage }}
          </p>
        </div>
        <div class="cardsRow" style="padding-bottom: 0px">
          <p class="text-muted text-left stat">
            <b>3PT -</b> {{ team.threePointers }}
          </p>
          <p class="text-muted text-left stat">
            <b>3PA -</b> {{ team.threePointerAttempts }}
          </p>
          <p class="text-muted text-left stat">
            <b>3P% -</b> {{ team.threePointerPercentage }}
          </p>
        </div>
        <hr />
        <div class="cardsRow">
          <p class="text-muted text-left stat">
            <b>RBs -</b> {{ team.totalRebounds }}
          </p>
          <p class="text-muted text-left stat">
            <b>O RBs -</b> {{ team.offensiveRebounds }}
          </p>
          <p class="text-muted text-left stat">
            <b>D RBs -</b> {{ team.defensiveRebounds }}
          </p>
        </div>
        <div class="cardsRow">
          <p class="text-muted text-left stat">
            <b>ASTs -</b> {{ team.assists }}
          </p>
          <p class="text-muted text-left stat">
            <b>STLs -</b> {{ team.steals }}
          </p>
          <p class="text-muted text-left stat">
            <b>BLKs -</b> {{ team.blocks }}
          </p>
        </div>
      </div>
    </div>
  </div>
  <div style="padding: 15px">
    <DataTable
      stripedRows
      scrollHeight="60vh"
      :value="teams"
      :paginator="true"
      :rows="10"
      dataKey="teamName"
      v-model:filters="filters"
      filterDisplay="menu"
      paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
      :rowsPerPageOptions="[10, 20, 50]"
      responsiveLayout="scroll"
      :globalFilterFields="['teamName']"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
      v-model:selection="selected"
      selectionMode="multiple"
      :metaKeySelection="false"
      @rowSelect="onRowSelect"
      @rowUnselect="onRowUnselect"
    >
      <template #header>
        <div class="flex justify-content-between">
          <Button
            type="button"
            icon="pi pi-filter-slash"
            label="Clear"
            class="p-button-outlined"
            @click="clearFilter()"
          />
          <span class="p-input-icon-left">
            <i class="pi pi-search" />
            <InputText
              v-model="filters['global'].value"
              placeholder="Keyword Search"
            />
          </span>
        </div>
        <div style="text-align: left">
          <MultiSelect
            :modelValue="selectedColumns"
            :options="columns"
            optionLabel="header"
            @update:modelValue="onToggle"
            placeholder="Select Columns"
            style="width: 20em"
          />
        </div>
      </template>
      <Column
        field="teamName"
        header="Team"
        :sortable="true"
        style="width: 30px"
      ></Column>
      <Column
        v-for="(col, index) of selectedColumns"
        :field="col.field"
        :header="col.header"
        :sortable="col.sortable"
        :key="col.field + '_' + index"
      ></Column>
    </DataTable>
  </div>
</template>

<script>
import teamsData from "../assets/data/TeamsPerGame.json";
import DataTable from "primevue/datatable";
import MultiSelect from "primevue/multiselect";
import InputText from "primevue/inputtext";
import Column from "primevue/column";
import Button from "primevue/button"; // Import the PrimeVue buttons.
import { FilterMatchMode, FilterOperator } from "primevue/api";
import "primeflex/primeflex.css"; // Import the PrimeVue layout utility library.

export default {
  components: {
    DataTable,
    Column,
    Button,
    MultiSelect,
    FilterMatchMode,
    FilterOperator,
    InputText,
  },
  data() {
    return {
      selectedColumns: null,
      columns: null,
      filters: null,
      selected: null,
      teams: teamsData,
    };
  },
  created() {
    this.columns = [
      { field: "gamesPlayed", header: "GP" },
      { field: "minutesPlayed", header: "MP" },
      { field: "fieldGoals", header: "FG" },
      { field: "fieldGoalAttempts", header: "FGA" },
      { field: "fieldGoalPercentage", header: "FG%" },
      { field: "threePointers", header: "3P", sortable: true },
      { field: "threePointerAttempts", header: "3PA", sortable: true },
      { field: "threePointerPercentage", header: "3P%", sortable: true },
      { field: "twoPointers", header: "2P" },
      { field: "twoPointerAttempts", header: "2PA" },
      { field: "twoPointerPercentage", header: "2P%" },
      { field: "freeThrows", header: "FT" },
      { field: "freeThrowAttempts", header: "FTA" },
      { field: "freeThrowPercentage", header: "FT%" },
      { field: "offensiveRebounds", header: "O RBs" },
      { field: "defensiveRebounds", header: "D RBs" },
      { field: "totalRebounds", header: "RBs", sortable: true },
      { field: "assists", header: "ASTs", sortable: true },
      { field: "steals", header: "STLs" },
      { field: "blocks", header: "BLKs" },
      { field: "turnovers", header: "TOs" },
      { field: "personalFouls", header: "PFs" },
      { field: "points", header: "PTS", sortable: true },
    ];
    this.selectedColumns = this.columns;
    this.initFilters();
  },
  methods: {
    onToggle(value) {
      this.selectedColumns = this.columns.filter((col) => value.includes(col));
    },
    clearFilter() {
      this.initFilters();
    },
    initFilters() {
      this.filters = {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        teamName: {
          operator: FilterOperator.AND,
          constraints: [{ value: null, matchMode: FilterMatchMode.IN }],
        },
      };
    },
    onRowSelect(event) {
      console.log("Selected " + event.data.teamName);
    },
    onRowUnselect(event) {
      console.log("Unselected " + event.data.teamName);
    },
  },
};
</script>

<style scoped>
.cardsRow {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
}
.stat {
  padding: 0px 20px 0px 0px;
}
</style>