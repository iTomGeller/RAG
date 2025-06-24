<template>
  <div class="profile-popover">
    <el-popover class="popover"  placement="bottom-start" trigger="hover" width="250px" offset="20" >
      <!-- content goes here -->
      <div class="profile-content">
        <!-- user information -->
        <div class="user-info">
          <img :src="user.avatar" alt="User Avatar" class="avatar" />
          <div class="user-details">
            <h3>{{ user.name }}</h3>
            <p>{{ user.email }}</p>
          </div>
        </div>

        <!-- user options -->
        <div class="profile-actions">
          <el-button class="act-btn"  @click="editProfile">Update Avatar</el-button>
          <el-button class="act-btn"  @click="logout">Logout</el-button>
        </div>
      </div>

      <!-- avatar popover -->
      <template #reference>
        <el-avatar :src="user.avatar" class="home-avatar" />
      </template>

    </el-popover>
  </div>
</template>

<script>
    export default{
        name : 'ProfileFloating',
        data(){
            return{
                user:{
                    name: 'Alex',
                    email: '123456',
                    avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
                }
            }
        }
    }
</script>



<style scoped>

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
}

.profile-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.user-info{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
  /* gap: 10px; */
}

.user-details {
  text-align: center;
}

.profile-actions{
  display: flex;
  flex-direction:column;
  align-items: center;
  gap:10px;

}


.act-btn{
  width: 150px;
}

.home-avatar {
  margin-top: 10px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
</style>