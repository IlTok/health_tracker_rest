<template id="product-overview">

  <app-layout>

    <div class="card bg-light mb-3">

      <div class="card-header">
        <div class="row">

          <div class="col-6">
            Products
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

        <form id="addProduct">

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-calories">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="formData.calories" name="calories" placeholder="Calories"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-proteins">Proteins</span>
            </div>
            <input type="text" class="form-control" v-model="formData.proteins" name="proteins" placeholder="Proteins"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-fats">Fats</span>
            </div>
            <input type="text" class="form-control" v-model="formData.fats" name="fats" placeholder="Fats"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-product-carbohydrates">Сarbohydrates</span>
            </div>
            <input type="text" class="form-control" v-model="formData.carbohydrates" name="carbohydrates" placeholder="Carbohydrates"/>
          </div>

        </form>

        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addProduct()">Add Product
        </button>

      </div>

    </div>

    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(product,name) in products" v-bind:key="name">

        <div class="mr-auto p-2">
          <span>
            <a :href="`/products/${product.name}`"> <span style="font-weight: bold;">{{ product.name }}</span> : {{ product.calories }} kcal, {{ product.proteins }} proteins, {{ product.fats }} fats, {{ product.carbohydrates }} carbohydrates per 100g</a>
          </span>
        </div>

        <div class="p2">

          <a :href="`/products/${product.name}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>

          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteProduct(product, name)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>

        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("product-overview", {
  template: "#product-overview",
  data: () => ({
    products: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchProducts();
  },
  methods: {

    fetchProducts: function () {
      axios.get("/api/products")
          .then(res => this.products = res.data)
          .catch(() => alert("Error while fetching Products"));
    },
    deleteProduct: function (product, index) {
      if (confirm('Are you sure you want to delete this product? This action cannot be undone.', 'Warning')) {
        const productName = product.name;
        const url = `/api/products/${productName}`;
        axios.delete(url)
            .then(response =>
                this.products.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addProduct: function () {
      const url = `/api/products`;
      axios.post(url,
          {
            name: this.formData.name,
            calories: this.formData.calories,
            proteins: this.formData.proteins,
            fats: this.formData.fats,
            carbohydrates: this.formData.carbohydrates,
          })
          .then(response => {
            this.products.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>