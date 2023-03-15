<template>
  <div style="padding: 10px">
    <h1>Players Per Game Stats</h1>
  </div>
  <a class="twitter-timeline" data-width="500" data-height="650" data-dnt="true" data-theme="dark"
    href="https://twitter.com/FantasyLabsNBA?ref_src=twsrc%5Etfw">Tweets by FantasyLabsNBA</a>
  <div style="padding: 15px">
    <DataTable stripedRows scrollHeight="60vh" :value="players" :paginator="true" :rows="10" dataKey="playerName"
      v-model:filters="filters" filterDisplay="menu"
      paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
      :rowsPerPageOptions="[10, 20, 50]" responsiveLayout="scroll" :globalFilterFields="['playerName']"
      currentPageReportTemplate="Showing {first} to {last} of {totalRecords}">
      <template #header>
        <div class="flex justify-content-between">
          <Button type="button" icon="pi pi-filter-slash" label="Clear" class="p-button-outlined"
            @click="clearFilter()" />
          <span class="p-input-icon-left">
            <i class="pi pi-search" />
            <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
          </span>
        </div>
        <div style="text-align: left">
          <MultiSelect :modelValue="selectedColumns" :options="columns" optionLabel="header"
            @update:modelValue="onToggle" placeholder="Select Columns" style="width: 20em" />
        </div>
      </template>
      <Column field="playerName" header="Player Name" :sortable="true"></Column>
      <Column v-for="(col, index) of selectedColumns" :field="col.field" :header="col.header" :sortable="col.sortable"
        :key="col.field + '_' + index"></Column>
    </DataTable>
  </div>
</template>

<script>
import playerData from "../assets/data/Players.json";
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
      players: playerData,
    };
  },
  created() {
    this.columns = [
      { field: "position", header: "POS" },
      { field: "age", header: "Age" },
      { field: "team", header: "Team" },
      { field: "gamesCount", header: "GP" },
      { field: "gamesStartedCount", header: "GS" },
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
      { field: "effectiveFieldGoalPercentage", header: "EFG%", sortable: true },
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
    let ckeditor = document.createElement("script");
    ckeditor.setAttribute("src", "https://platform.twitter.com/widgets.js");
    document.head.appendChild(ckeditor);
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
        playerName: {
          operator: FilterOperator.AND,
          constraints: [{ value: null, matchMode: FilterMatchMode.IN }],
        },
      };
    },
  },
};
</script>