<template>
  <div class="profile-popover">
    <el-popover
      popper-class="my-popover"
      placement="bottom-start"
      trigger="click"
      width="250px"
      offset="20"
      :popper-style ="{borderRadius: '30px'}"
    >
      <!-- content goes here -->
      <div class="profile-content">
        <!-- user information -->
        <div class="user-info">
          <img :src="user.avatarUrl" alt="User Avatar" class="avatar" />
          <div class="user-details">
            <h3>{{ user.username }}</h3>
            <p>{{ user.email }}</p>
          </div>
        </div>

        <!-- user options -->
        <div class="profile-actions">
          <div class="btn-box">
          <!-- <el-button class="act-btn" @click="editProfile">Update Avatar</el-button> -->
          <AvatarUpload />
          <el-button class="act-btn" @click="logout">退出</el-button>
          </div>
        </div>
      </div>

      <!-- avatar popover -->
      <template #reference>
        <el-avatar :src="user.avatarUrl" class="home-avatar" />
      </template>
    </el-popover>
  </div>
</template>

<script>
import AvatarUpload from './AvatarUpload.vue';

export default {
  name: 'ProfileFloating',
  components: {AvatarUpload},
  data() {
    return {
      user: {
        username: ' ',
        email: ' ',
        avatarUrl: ' ',
      },
    }
  },
  mounted() {
    const userInfo = localStorage.getItem('userInfo');
    if (userInfo) {
      this.user = JSON.parse(userInfo);
    }
  },
  methods: {
    editProfile() {
      console.log('edit profile');
      },
      logout() {
        localStorage.removeItem('userInfo');
        location.reload(); //跳转
      },
  }
}
</script>

<!--popper元素挂载在body下，故在scoped区域下无法修改style -->

<style scoped>
/* .my-popover {
  --el-popover-border-radius: 50px;
} */

.avatar {
  width: 60px;
  height: 60px;
  border: 0.5px solid rgb(46, 5, 87);
  border-radius: 50%;
}

.profile-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 30px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
  /* gap: 10px; */
}

.user-details {
  text-align: center;
}

.profile-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.btn-box  .act-btn {
  flex: 1;
  margin: 0 !important;
  border: none;
  
}

.home-avatar {
  margin-top: 10px;
  width: 40px;
  height: 40px;
  border: 0.5px solid rgb(46, 5, 87);
  border-radius: 50%;
  background-color: transparent;
}

.btn-box {
  background-color: #f5f5f5;
  border-radius: 30px;
  display: flex;
  flex-direction: column;
  height: 100px;
  width: 210px;
  gap: 5px solid hsl(0, 7%, 9%);
}

.act-btn {
  height: 50px;
  width: 220px;
  margin:0 !important;
  padding: 0 !important;
 
  background-color: transparent;
}
</style>
