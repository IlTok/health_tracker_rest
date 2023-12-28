<template id="sleep-overview">

  <app-layout>

    <div class="card bg-light mb-3">

      <div class="card-header">
        <div class="row">

          <div class="col-6">
            Sleeps
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
              <span class="input-group-text" id="input-sleep-duration">Duration</span>
            </div>
            <input type="text" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="formData.date" name="date" placeholder="Date"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-userId">User Id</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User Id"/>
          </div>

        </form>

        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addSleep()">Add Sleep
        </button>

      </div>

    </div>

    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(sleep,index) in sleeps" v-bind:key="index">

        <div class="mr-auto p-2">
          <span><a :href="`/sleeps/${sleep.id}`"> {{ sleep.duration }} hours slept user with Id {{ sleep.userId }}</a></span>
        </div>

        <div class="p2">

          <a :href="`/sleeps/${sleep.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>

          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteSleep(sleep, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>

        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("sleep-overview", {
  template: "#sleep-overview",
  data: () => ({
    sleeps: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchSleeps();
  },
  methods: {
    fetchSleeps: function () {
      axios.get("/api/sleeps")
          .then(res => this.sleeps = res.data)
          .catch(() => alert("Error while fetching Sleeps"));
    },
    deleteSleep: function (sleep, index) {
      if (confirm('Are you sure you want to delete this sleep? This action cannot be undone.', 'Warning')) {
        const sleepId = sleep.id;
        const url = `/api/sleeps/sleep/${sleepId}`;
        axios.delete(url)
            .then(response =>
                this.sleeps.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addSleep: function () {
      const url = `/api/sleeps`;
      axios.post(url,
          {
            duration: this.formData.duration,
            date: this.formData.date,
            userId: this.formData.userId,
          })
          .then(response => {
            this.sleeps.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>