<template>
  <div class="profile-popover">
    <el-popover
      popper-class="my-popover"
      placement="bottom-start"
      trigger="hover"
      width="250px"
      offset="20"
      :popper-style ="{borderRadius: '30px'}"
    >

      <!-- content goes here -->
      <div class="profile-content">
        <!-- user information -->
        <div class="user-info">
          <img :src="user.avatarUrl " alt="User Avatar" class="avatar" />
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
          <!-- 组合式api内部，不需要导入，使用this点取的语法 -->
          <el-button  class="act-btn" @click="logout">{{ this.$t('profile.logout') }}</el-button>
          </div>
        </div>
      </div>

      <!-- avatar popover -->
      <template #reference>
        <el-avatar :src="user.avatarUrl" class="home-avatar" alt="Avatar"/>
        <p> helllo </p>
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
<!-- 使用 :popper-style="{ borderRadius: '30px' }" 控制边框圆角 -->

<style scoped>

.avatar {
  width: 60px;
  height: 60px;
  border: 0.5px solid rgb(255, 255, 255);
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
  /* margin: 0 !important; */
  border: none;
  
}

.home-avatar {
  margin-top: 10px;
  width: 60px;
  height: 60px;
  border: 0.5px solid rgb(255, 255, 255);
  border-radius: 50%;
  background-color: transparent;
}

.btn-box {
  /* background-color: #f5f5f5; */
  border-radius: 30px;
  display: flex;
  flex-direction: column;
  height: 100px;
  width: 210px;
  gap: 5px solid hsl(0, 7%, 9%);
}

.act-btn {
  height: 50px;
  width: 200px;
  margin:0 0 0 5px !important;
  padding: 0 !important;
  background-color: transparent;
  border-radius: 20px;
}
</style>
