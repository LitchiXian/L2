<template>
  <div>
    <div v-if="loading" class="loading-container">
      <div class="loading">Loading...</div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-container">
      <p>⚠️ {{ error }}</p>
      <button @click="retryFetch">重试</button>
    </div>

    <div v-else-if="currentPost" class="post-container">
      <article class="post">
        <header class="post-header">
          <h1 class="post-title">{{ currentPost.title }}</h1>

          <div class="post-meta">
            <span class="post-date">⏱️ {{ formatDate(currentPost.createTime) }}</span>
          </div>
        </header>

        <div class="post-content">
          <MarkdownRenderer
              :content="currentPost.content"
              @toc-generated="handleTocGenerated"
          />
        </div>
      </article>
    </div>

    <!-- 文章不存在 -->
    <div v-else class="not-found">
      <h2>文章不存在</h2>
      <p>抱歉，找不到您要查看的文章</p>
      <router-link to="/">返回首页</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {useRoute} from 'vue-router'
import MarkdownRenderer from '@/component/MarkdownRenderer.vue'
import {getArticle} from "@/api/web/blogArticle.ts";
import type {Article} from "@/type/article.ts";

const route = useRoute()
const currentPost = ref<Article>()
const loading = ref(true)
const error = ref(null)

// 获取文章详情数据
const fetchArticle = async () => {
  try {
    loading.value = true
    error.value = null

    // 调用API接口获取数据
    currentPost.value = await getArticle({id: route.params.id});

  } finally {
    loading.value = false
  }
}

const handleTocGenerated = (items: any) => {
  // 设置TOC目录项
  window.dispatchEvent(new CustomEvent('set-toc', {detail: items}));
}

/**
 * 将时间戳转换为格式化的日期时间字符串 (YYYY-MM-DD HH:mm)
 * @param {number} timestamp - 时间戳（毫秒）
 * @returns {string} 格式化后的日期时间字符串
 */
const formatDate = (timestamp: number) => {
  // 创建日期对象
  const date = new Date(timestamp);

  // 获取日期组件
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  // 组合成格式化字符串
  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 监听路由参数变化
watch(() => route.params.id, (newId) => {
  if (newId) fetchArticle()
})

// 重试获取数据
const retryFetch = () => {
  fetchArticle()
}

onMounted(() => {

  fetchArticle()

  // 监听设置TOC事件
  window.addEventListener('set-toc', (event) => {
    if (window.$mainLayout) {
      window.$mainLayout.setTocItems(event.detail);
    }
  });

  // 保存布局组件的引用以便访问其方法
  if (window.$mainLayout) {
    window.$mainLayout = null;
  }
});
</script>

<style scoped>
.post-container {
  max-width: 900px;
  margin: 0 auto;
}

.post-header {
  margin-bottom: 40px;
}

.post-title {
  font-size: 2.25rem;
  color: #e2e2ec; /* 226,226,236 - 标题颜色 */
  margin-bottom: 15px;
  line-height: 1.2;
}

.post-subtitle {
  font-size: 1.25rem;
  color: #a0a0a0;
  margin-bottom: 25px;
}

.post-meta {
  display: flex;
  gap: 20px;
  color: #8e8e8e;
  font-size: 0.95rem;
}

.post-date, .post-read-time {
  display: flex;
  align-items: center;
}

.post-content {
  font-size: 1.1rem;
  line-height: 1.8;
}

/* 新增状态样式 */
.loading-container {
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
  padding: 50px 0;
}

.loader {
  border: 5px solid #f3f3f3;
  border-top: 5px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.error-container {
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
  padding: 40px;
  background-color: #ffebee;
  border-radius: 8px;
}

.error-container button {
  margin-top: 15px;
  padding: 8px 20px;
  background-color: #e53935;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.error-container button:hover {
  background-color: #c62828;
}

.not-found {
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
  padding: 60px 20px;
}

.not-found a {
  color: #42b983;
  text-decoration: none;
  margin-top: 20px;
  display: inline-block;
}

.not-found a:hover {
  text-decoration: underline;
}
</style>