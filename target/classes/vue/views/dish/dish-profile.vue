<template id="dish-profile">

  <app-layout>

    <div v-if="noDishFound">
      <p> We're sorry, we were not able to retrieve this dish.</p>
      <p> View <a :href="'/dishes'">all dishes</a>.</p>
    </div>

    <div class="card bg-light mb-3" v-if="!noDishFound">

      <div class="card-header">

        <div class="row">

          <div class="col-6"> Dish </div>
          <div class="col" align="right">

            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateDish()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>

            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteDish()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>

          </div>

        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="dish.name" name="description" placeholder="Name"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-ingredient">Ingredient</span>
            </div>
            <input type="text" class="form-control" v-model="dish.ingredient" name="ingredient" placeholder="Ingredient"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-weight">Weight</span>
            </div>
            <input type="text" class="form-control" v-model="dish.weight" name="weight" placeholder="Weight"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-dish-calories">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="dish.calories" name="calories" placeholder="Calories"/>
          </div>

        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("dish-profile", {
  template: "#dish-profile",
  data: () => ({
    dish: null,
    noDishFound: false,
    dishes: [],
  }),
  created: function () {
    const dishName = this.$javalin.pathParams["dish-name"];
    const url = `/api/dishes/${dishName}`
    axios.get(url)
        .then(res => this.dish = res.data)
        .catch(error => {
          console.log("No product found for id passed in the path parameter: " + error)
          this.noDishFound = true
        })
  },

  methods: {

    updateDish: function () {
      const dishName = this.$javalin.pathParams["dish-name"];
      const url = `/api/dishes/${dishName}`
      axios.patch(url,
          {
            name: this.formData.name,
            ingredient: this.formData.ingredient,
            weight: this.formData.weight,
            calories: this.formData.calories,
          })
          .then(response =>
              this.dish.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Dish was updated!")
    },

    deleteDish: function () {
      if (confirm("Do you really want to delete?")) {
        const dishName = this.$javalin.pathParams["dish-name"];
        const url = `/api/dishes/${dishName}`
        axios.delete(url)
            .then(response => {
              alert("Dish was deleted")
              window.location.href = '/dishes';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>