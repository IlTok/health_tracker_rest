<template id="product-profile">

  <app-layout>

    <div v-if="noProductFound">
      <p> We're sorry, we were not able to retrieve this product.</p>
      <p> View <a :href="'/products'">all products</a>.</p>
    </div>

    <div class="card bg-light mb-3" v-if="!noProductFound">

      <div class="card-header">

        <div class="row">

          <div class="col-6"> Product </div>
          <div class="col" align="right">

            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateProduct()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>

            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteProduct()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>

          </div>

        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="product.name" name="description" placeholder="Name"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-calories">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="product.calories" name="calories" placeholder="Calories"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-proteins">Proteins</span>
            </div>
            <input type="text" class="form-control" v-model="product.proteins" name="proteins" placeholder="Proteins"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-fats">Fats</span>
            </div>
            <input type="text" class="form-control" v-model="product.fats" name="fats" placeholder="Fats"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-carbohydrates">Carbohydrates</span>
            </div>
            <input type="text" class="form-control" v-model="product.carbohydrates" name="carbohydrates" placeholder="Carbohydrates"/>
          </div>

        </form>
      </div>

      <div class="card-footer text-left">
        <p  v-if="dishes.length === 0"> No dishes yet...</p>
        <p  v-if="dishes.length > 0"> Dishes with this product so far...</p>
        <ul>
          <li v-for="dish in dishes">
            {{ dish.ingredient }} : {{ dish.weight }} grammes ; {{ dish.calories }} kcal
          </li>
        </ul>
      </div>

    </div>
  </app-layout>
</template>

<script>
app.component("product-profile", {
  template: "#product-profile",
  data: () => ({
    product: null,
    noProductFound: false,
    products: [],
    dishes: [],
  }),
  created: function () {
    const productName = this.$javalin.pathParams["product-name"];
    const url = `/api/products/${productName}`
    axios.get(url)
        .then(res => this.product = res.data)
        .catch(error => {
          console.log("No product found for id passed in the path parameter: " + error)
          this.noProductFound = true
        })

    const dishesUrl = `/api/dishes/ingredient/${productName}`
    axios.get(dishesUrl)
        .then(res => this.dishes = res.data)
        .catch(error => {
          console.log("No dishes found for id passed in the path parameter: " + error)
        })
  },

  methods: {
    updateProduct: function () {
      const productName = this.$javalin.pathParams["product-name"];
      const url = `/api/products/${productName}`
      axios.patch(url,
          {
            name: this.formData.name,
            calories: this.formData.calories,
            proteins: this.formData.proteins,
            fats: this.formData.fats,
            carbohydrates: this.formData.carbohydrates,
          })
          .then(response =>
              this.product.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Product was updated!")
    },
    deleteProduct: function () {
      if (confirm("Do you really want to delete?")) {
        const productName = this.$javalin.pathParams["product-name"];
        const url = `/api/products/${productName}`
        axios.delete(url)
            .then(response => {
              alert("Product was deleted")
              window.location.href = '/products';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>