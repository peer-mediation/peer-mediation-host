/// <reference types="vitest" />
import react from "@vitejs/plugin-react";
import { defineConfig, loadEnv } from "vite";

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), "");
  return {
    build: {
      outDir: "build",
    },
    define: {
      // "process.env.API_KEY": JSON.stringify(env.API_KEY),
      // "process.env.AUTH_DOMAIN": JSON.stringify(env.AUTH_DOMAIN),
      // "process.env.PROJECT_ID": JSON.stringify(env.PROJECT_ID),
      // "process.env.STORAGE_BUCKET": JSON.stringify(env.STORAGE_BUCKET),
      // "process.env.MESSAGING_SENDER_ID": JSON.stringify(
      //   env.MESSAGING_SENDER_ID
      // ),
      // "process.env.APP_ID": JSON.stringify(env.APP_ID),
      // "process.env.MEASUREMENT_ID": JSON.stringify(env.MEASUREMENT_ID),
      // "process.env.ADMIN_USERNAME": JSON.stringify(env.ADMIN_USERNAME),
      // "process.env.ADMIN_PASSWORD": JSON.stringify(env.ADMIN_PASSWORD),
      // "process.env.ADMIN_EMAIL": JSON.stringify(env.ADMIN_EMAIL),
      // "process.env.USER_EMAIL": JSON.stringify(env.USER_EMAIL)
    },
    plugins: [react()],
    server: {
      port: 8000,
    },
    test: {
      exclude: ["**/e2e/**", "**/node_modules/**"],
    },
  };
});
