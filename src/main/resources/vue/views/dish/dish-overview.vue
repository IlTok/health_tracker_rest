<template id="dish-overview">

  <app-layout>

    <div class="card bg-light mb-3">

      <div class="card-header">
        <div class="row">

          <div class="col-6">
            Dishes
          </div>

          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>

        </div>
      </div>

      <div class="card-body" :class="{ 'd-none': hideForm}">

        <form id="addDish">

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-ingredient">Ingredient</span>
            </div>
            <input type="text" class="form-control" v-model="formData.ingredient" name="ingredient" placeholder="Ingredient"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-weight">Weight</span>
            </div>
            <input type="text" class="form-control" v-model="formData.weight" name="weight" placeholder="Weight"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-fats">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="formData.calories" name="fats" placeholder="Calories"/>
          </div>

        </form>

        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addDish()">Add Dish
        </button>

      </div>

    </div>

    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(dish,name) in dishes" v-bind:key="name">

        <div class="mr-auto p-2">
          <span>
            <a :href="`/dishes/${dish.name}`"> <span style="font-weight: bold;">{{ dish.name }}</span> consists of {{ dish.ingredient }} ; {{ dish.weight }} grammes, {{ dish.calories}} kcal</a>
          </span>
        </div>

        <div class="p2">

          <a :href="`/dishes/${dish.name}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>

          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteDish(dish, name)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>

        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("dish-overview", {
  template: "#dish-overview",
  data: () => ({
    dishes: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchDishes();
  },
  methods: {

    fetchDishes: function () {
      axios.get("/api/dishes")
          .then(res => this.dishes = res.data)
          .catch(() => alert("Error while fetching Dishes"));
    },

    deleteDish: function (dish, index) {
      if (confirm('Are you sure you want to delete this dish? This action cannot be undone.', 'Warning')) {
        const dishName = dish.name;
        const url = `/api/dishes/${dishName}`;
        axios.delete(url)
            .then(response =>
                this.dishes.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },

    addDish: function () {
      const url = `/api/dishes`;
      axios.post(url,
          {
            name: this.formData.name,
            ingredient: this.formData.ingredient,
            weight: this.formData.weight,
            calories: this.formData.calories
          })
          .then(response => {
            this.dishes.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>