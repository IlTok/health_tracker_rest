<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>

    <div class="row">

      <div class="col mb-4">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

      <div class="col mb-4">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>

    <div class="row">

      <div class="col mb-4">
        <div class="card">
          <h5 class="card-header">Total Products</h5>
          <div class="card-body">
            <h5 class="card-title">{{products.length}} products</h5>
            <a href="/products" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

      <div class="col mb-4">
        <div class="card">
          <h5 class="card-header">Total Dishes</h5>
          <div class="card-body">
            <h5 class="card-title">{{dishes.length}} dishes</h5>
            <a href="/dishes" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

    </div>

    <div class="row">

      <div class="col mb-4">
        <div class="card">
          <h5 class="card-header">Total Purchases</h5>
          <div class="card-body">
            <h5 class="card-title">{{purchases.length}} purchases</h5>
            <a href="/purchases" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

      <div class="col mb-4">
        <div class="card">
          <h5 class="card-header">Total Sleeps</h5>
          <div class="card-body">
            <h5 class="card-title">{{sleeps.length}} sleeps</h5>
            <a href="/sleeps" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

    </div>

  </app-layout>
</template>

<script>
app.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        products:[],
        dishes:[],
        purchases:[],
        sleeps:[]
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching Users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching Activities"));
        axios.get("/api/products")
            .then(res => this.products = res.data)
            .catch(() => alert("Error while fetching Products"));
        axios.get("/api/dishes")
            .then(res => this.dishes = res.data)
            .catch(() => alert("Error while fetching Dishes"));
        axios.get("/api/purchases")
            .then(res => this.purchases = res.data)
            .catch(() => alert("Error while fetching Purchases"));
        axios.get("/api/sleeps")
            .then(res => this.sleeps = res.data)
            .catch(() => alert("Error while fetching Sleeps"));
      }
    });
</script>