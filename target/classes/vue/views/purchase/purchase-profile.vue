<template id="purchase-profile">

  <app-layout>

    <div v-if="noPurchaseFound">
      <p> We're sorry, we were not able to retrieve this purchase.</p>
      <p> View <a :href="'/purchases'">all purchases</a>.</p>
    </div>

    <div class="card bg-light mb-3" v-if="!noPurchaseFound">

      <div class="card-header">

        <div class="row">

          <div class="col-6"> Purchase </div>
          <div class="col" align="right">

            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updatePurchase()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>

            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deletePurchase()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>

          </div>

        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-id">Purchase ID</span>
            </div>
            <input type="number" class="form-control" v-model="purchase.id" name="id" readonly placeholder="Id"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-userId">User Id</span>
            </div>
            <input type="text" class="form-control" v-model="purchase.userId" name="userId" placeholder="User Id"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-productName">Product name</span>
            </div>
            <input type="text" class="form-control" v-model="purchase.productName" name="productName" placeholder="Product name"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-price">Price</span>
            </div>
            <input type="text" class="form-control" v-model="purchase.price" name="price" placeholder="Price"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-date">Date</span>
            </div>
            <input type="text" class="form-control" v-model="purchase.date" name="date" placeholder="Date"/>
          </div>

        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("purchase-profile", {
  template: "#purchase-profile",
  data: () => ({
    purchase: null,
    noPurchaseFound: false,
    purchases: [],
  }),
  created: function () {
    const purchaseId = this.$javalin.pathParams["purchase-id"];
    const url = `/api/purchases/${purchaseId}`
    axios.get(url)
        .then(res => this.purchase = res.data)
        .catch(error => {
          console.log("No purchase found for id passed in the path parameter: " + error)
          this.noActivityFound = true
        })
  },

  methods: {

    updatePurchase: function () {
      const purchaseId = this.$javalin.pathParams["purchase-id"];
      const url = `/api/purchases/${purchaseId}`
      axios.patch(url,
          {
            userId: this.formData.userId,
            productName: this.formData.productName,
            price: this.formData.price,
            date: this.formData.date,
          })
          .then(response =>
              this.purchase.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Purchase was updated!")
    },

    deletePurchase: function () {
      if (confirm("Do you really want to delete?")) {
        const purchaseId = this.$javalin.pathParams["purchase-id"];
        const url = `/api/purchases/${purchaseId}`
        axios.delete(url)
            .then(response => {
              alert("Purchase was deleted")
              window.location.href = '/purchases';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>