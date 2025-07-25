<script setup lang="ts">
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {useUserStore} from "@/store/user.ts";
import {showMessage} from "@/util/message.ts";

// const {proxy} = getCurrentInstance();

/*--------------- 响应式状态声明 ---------------*/
// 邮箱输入值
const email = ref<string>('');
// 密码输入值
const password = ref<string>('');
// 密码可见状态
const isPasswordVisible = ref<boolean>(false);

const router = useRouter();
const route = useRoute();
const loginButtonRef = ref<HTMLButtonElement | null>(null);
const loading = ref<boolean>(false);

/*--------------- 密码可见性切换方法 ---------------*/
const togglePasswordVisibility = () => {
  isPasswordVisible.value = !isPasswordVisible.value;
};

/*--------------- 表单提交处理（示例） ---------------*/
const handleSubmit = async (e: Event) => {
  e.preventDefault(); // 阻止表单默认提交
  console.log('登录信息:', {email: email.value, password: password.value});

  loading.value = true;
  if (loginButtonRef.value) {
    loginButtonRef.value.disabled = true;
    loginButtonRef.value.textContent = '登录中...';
  }

  try {
    const userStore = useUserStore();
    await userStore.login({
      userName: email.value,
      password: password.value,
      redirect: route.query.redirect as string
    })
    showMessage('登录成功', 'success');
  }finally {
    loading.value = false;
    if (loginButtonRef.value) {
      loginButtonRef.value.disabled = false;
      loginButtonRef.value.textContent = 'Login';
    }
  }

};
</script>

<template>
  <div class="login-container">
    <!-- 背景图（取消注释启用） -->
    <img src="@/asset/image/login-bg.png" alt="登录背景" class="login-bg">

    <form @submit="handleSubmit" class="login-form">
      <h1 class="login-title">Login</h1>

      <div class="login-content">
        <!-- 邮箱输入框 -->
        <div class="login-box">
          <i class="ri-user-3-line login-icon"></i>
          <div class="login-box-input">
            <input
                type="text"
                required
                class="login-input"
                id="login-email"
                placeholder=" "
                v-model="email"
            >
            <label for="login-email" class="login-label">Email</label>
          </div>
        </div>

        <!-- 密码输入框（核心优化） -->
        <div class="login-box">
          <i class="ri-lock-2-line login-icon"></i>
          <div class="login-box-input">
            <input
                :type="isPasswordVisible ? 'text' : 'password'"
                required
                class="login-input"
                id="login-pass"
                placeholder=" "
                v-model="password"
            >
            <label for="login-pass" class="login-label">Password</label>
            <!-- 密码可见切换图标 -->
            <el-icon
                :class="['login-eye']"
                @click="togglePasswordVisibility"
            >
              <Hide v-if="isPasswordVisible"/>
              <View v-else/>
            </el-icon>
          </div>
        </div>
      </div>

      <div class="login-check">
        <div class="login-check-group">
          <input type="checkbox" class="login-check-input" id="login-check">
          <label for="login-check" class="login-check-label">Remember me</label>
        </div>
        <!--        <a href="#" class="login-forgot">Forgot Password?</a>-->
      </div>

      <button type="submit" class="login-button">Login</button>

      <p class="login-register">
        Don't have an account?
        <router-link to="/register" class="login-register-link">Register</router-link>
        <!--        <a href="#" class="login-register-link">Register</a>-->
      </p>
    </form>
  </div>
</template>

<style scoped>
/*=============== 组件基础样式 ===============*/
.login-container {
  position: relative;
  height: 100vh;
  display: grid;
  align-items: center;
  justify-content: center;
  /* 防止子元素溢出 */
  overflow: hidden;
}

.login-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  opacity: 0.9; /* 背景图透明度 */
}

.login-form {
  position: relative;
  background: rgba(255, 255, 255, 0.05); /* 半透明背景 */
  backdrop-filter: blur(10px); /* 毛玻璃效果 */
  border: 1px solid rgba(255, 255, 255, 0.1); /* 边框 */
  border-radius: 15px;
  padding: 2.5rem;
  width: 90%;
  max-width: 400px; /* 最大宽度 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  /* 限制组件内样式作用范围 */
  z-index: 1; /* 确保表单在背景图上方 */
}

.login-title {
  text-align: center;
  color: #ffffff;
  font-size: 1.8rem;
  margin-bottom: 2rem;
  font-weight: 500;
}

.login-content {
  display: grid;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.login-box {
  display: grid;
  grid-template-columns: 30px 1fr; /* 图标固定宽度 */
  align-items: center;
  gap: 0.75rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.login-icon {
  color: #ffffff;
  font-size: 1.25rem;
}

.login-input {
  width: 100%;
  padding: 0.8rem 0;
  background: transparent;
  color: #ffffff;
  font-size: 1rem;
  border: none;
  outline: none;
  transition: 0.3s;
}

.login-box-input {
  position: relative;
}

.login-label {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.6);
  pointer-events: none;
  transition: 0.3s;
}

.login-input:focus ~ .login-label,
.login-input:not(:placeholder-shown) ~ .login-label {
  top: -5px;
  font-size: 0.8rem;
  color: #ffffff;
}

.login-eye {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  transition: 0.3s;
}

.login-eye:hover {
  color: #ffffff;
}

.login-check {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.login-check-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.login-check-input {
  width: 16px;
  height: 16px;
  accent-color: #ffffff; /* 复选框选中颜色 */
}

.login-forgot {
  color: rgba(255, 255, 255, 0.6);
  transition: 0.3s;
}

.login-forgot:hover {
  color: #ffffff;
  text-decoration: underline;
}

.login-button {
  width: 100%;
  padding: 1rem;
  background: #ffffff;
  color: #764ba2;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: 0.3s;
}

.login-button:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
}

.login-register {
  text-align: center;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 1rem;
}

.login-register-link {
  color: #ffffff;
  font-weight: 500;
  transition: 0.3s;
}

.login-register-link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (min-width: 768px) {
  .login-form {
    padding: 3rem;
    max-width: 450px;
  }

  .login-title {
    font-size: 2rem;
  }
}
</style>