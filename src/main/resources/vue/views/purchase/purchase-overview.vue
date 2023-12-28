<template id="purchase-overview">

  <app-layout>

    <div class="card bg-light mb-3">

      <div class="card-header">
        <div class="row">

          <div class="col-6">
            Purchases
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

        <form id="addPurchase">

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-userid">UserId</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-productName">ProductName</span>
            </div>
            <input type="text" class="form-control" v-model="formData.productName" name="productName" placeholder="Product Name"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-price">Price</span>
            </div>
            <input type="text" class="form-control" v-model="formData.price" name="price" placeholder="Price"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-purchase-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="formData.date" name="date" placeholder="Date"/>
          </div>

        </form>

        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addPurchase()">Add Purchase
        </button>

      </div>

    </div>

    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(purchase,index) in purchases" v-bind:key="index">

        <div class="mr-auto p-2">
          <span><a :href="`/purchases/${purchase.id}`"> {{ purchase.productName }} ; price {{ purchase.price }} ; user Id {{ purchase.userId }}</a></span>
        </div>

        <div class="p2">

          <a :href="`/purchases/${purchase.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>

          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deletePurchase(purchase, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>

        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("purchase-overview", {
  template: "#purchase-overview",
  data: () => ({
    purchases: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchPurchases();
  },
  methods: {
    fetchPurchases: function () {
      axios.get("/api/purchases")
          .then(res => this.purchases = res.data)
          .catch(() => alert("Error while fetching Purchases"));
    },
    deletePurchase: function (purchase, index) {
      if (confirm('Are you sure you want to delete this purchase? This action cannot be undone.', 'Warning')) {
        const purchaseId = purchase.id;
        const url = `/api/purchases/${purchasesId}`;
        axios.delete(url)
            .then(response =>
                this.purchases.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addPurchase: function () {
      const url = `/api/purchases`;
      axios.post(url,
          {
            userId: this.formData.userId,
            productName: this.formData.productName,
            price: this.formData.price,
            date: this.formData.date,
          })
          .then(response => {
            this.purchases.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>