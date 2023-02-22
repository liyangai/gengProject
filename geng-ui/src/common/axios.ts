import { HttpResult } from "@/model/HttpResultModel";
import axios, { AxiosResponse } from "axios";
import { isDevMode } from "./common-func.tion";
import { HttpCode } from "./constant/HttpCode";
import { ElMessage } from "element-plus";
const baseUrl = isDevMode() ? "/api" : "";
const API = axios.create({
  baseURL: baseUrl,
});

// API.interceptors.response.use(
//   (value: AxiosResponse<any, any>): AxiosResponse<any, any> => {
//     const data = value.data;
//   },
//   (err) => {}
// );
// eslint-disable-next-line @typescript-eslint/no-explicit-any
API.interceptors.response.use((data: any) => {
  const value = data.data;
  if (value.code === HttpCode.SUCCESS) {
    return value;
  } else {
    ElMessage.error(value.msg);
    return null;
  }
});

export { API };
