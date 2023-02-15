const { defineConfig } = require("@vue/cli-service");
const path = require("path");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      // 如果请求地址以/api打头,就出触发代理机制
      // http://localhost:8080/api/login -> http://localhost:3000/api/login
      "/api": {
        target: "http://localhost:80", // 我们要代理的真实接口地址
        changeOrigin: true,
        ws: true,
        pathRewrite: { "^/api": "" },
      },
    },
  },
  outputDir: "../geng-backend/src/main/resources/static",
  // output: {
  //   path: path.resolve(_dirname,'../geng-backend/src/main/resources/static/page')
  // }
});
