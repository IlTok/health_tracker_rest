<template id="activity-profile">

  <app-layout>

    <div v-if="noActivityFound">
      <p> We're sorry, we were not able to retrieve this activity.</p>
      <p> View <a :href="'/activities'">all activities</a>.</p>
    </div>

    <div class="card bg-light mb-3" v-if="!noActivityFound">

      <div class="card-header">

        <div class="row">

          <div class="col-6"> Activity </div>
          <div class="col" align="right">

            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateActivity()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>

            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteActivity()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>

          </div>

        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Activity ID</span>
            </div>
            <input type="number" class="form-control" v-model="activity.id" name="id" readonly placeholder="Id"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="activity.description" name="description" placeholder="Description"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-duration">Duration</span>
            </div>
            <input type="text" class="form-control" v-model="activity.duration" name="duration" placeholder="Duration"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-calories">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="activity.calories" name="calories" placeholder="Calories"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-started">Started</span>
            </div>
            <input type="text" class="form-control" v-model="activity.started" name="started" placeholder="Started"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-userId">UserId</span>
            </div>
            <input type="text" class="form-control" v-model="activity.userId" name="userId" placeholder="User ID"/>
          </div>

        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null,
    noActivityFound: false,
    activities: [],
  }),
  created: function () {
    const activityId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${activityId}`
    axios.get(url)
        .then(res => this.activity = res.data)
        .catch(error => {
          console.log("No activity found for id passed in the path parameter: " + error)
          this.noActivityFound = true
        })
  },

  methods: {
    updateActivity: function () {
      const activityId = this.$javalin.pathParams["activity-id"];
      const url = `/api/activities/${activityId}`
      axios.patch(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: this.formData.started,
            userId: this.formData.userId
          })
          .then(response =>
              this.activity.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Activity was updated!")
    },
    deleteActivity: function () {
      if (confirm("Do you really want to delete?")) {
        const activityId = this.$javalin.pathParams["activity-id"];
        const url = `/api/activities/${activityId}`
        axios.delete(url)
            .then(response => {
              alert("Activity was deleted")
              window.location.href = '/activities';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>