<template id="sleep-profile">

  <app-layout>

    <div v-if="noSleepFound">
      <p> We're sorry, we were not able to retrieve this sleep.</p>
      <p> View <a :href="'/sleeps'">all sleeps</a>.</p>
    </div>

    <div class="card bg-light mb-3" v-if="!noSleepFound">

      <div class="card-header">

        <div class="row">

          <div class="col-6"> Sleep </div>
          <div class="col" align="right">

            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateSleep()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>

            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteSleep()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>

          </div>

        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-id">Sleep ID</span>
            </div>
            <input type="number" class="form-control" v-model="sleep.id" name="id" readonly placeholder="Id"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-duration">Duration</span>
            </div>
            <input type="text" class="form-control" v-model="sleep.duration" name="duration" placeholder="Duration"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-date">Date</span>
            </div>
            <input type="text" class="form-control" v-model="sleep.date" name="date" placeholder="Date"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-userid">UserId</span>
            </div>
            <input type="text" class="form-control" v-model="sleep.userId" name="userid" placeholder="User ID"/>
          </div>

        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("sleep-profile", {
  template: "#sleep-profile",
  data: () => ({
    sleep: null,
    noSleepFound: false,
    sleeps: [],
  }),
  created: function () {
    const sleepId = this.$javalin.pathParams["sleep-id"];
    const url = `/api/sleeps/sleep/${sleepId}`
    axios.get(url)
        .then(res => this.sleep = res.data)
        .catch(error => {
          console.log("No sleep found for id passed in the path parameter: " + error)
          this.noSleepFound = true
        })
  },

  methods: {
    updateSleep: function () {
      const sleepId = this.$javalin.pathParams["sleep-id"];
      const url = `/api/sleeps/sleep/${sleepId}`
      axios.patch(url,
          {
            duration: this.formData.duration,
            date: this.formData.date,
            userId: this.formData.userId,
          })
          .then(response =>
              this.sleep.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Sleep was updated!")
    },
    deleteSleep: function () {
      if (confirm("Do you really want to delete?")) {
        const sleepId = this.$javalin.pathParams["sleep-id"];
        const url = `/api/sleeps/sleep/${sleepId}`
        axios.delete(url)
            .then(response => {
              alert("Sleep was deleted")
              window.location.href = '/sleeps';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>