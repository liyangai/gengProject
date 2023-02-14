export class HttpResult<T> {
  constructor(data: T, code: number, msg: string | null) {
    this.data = data;
    this.code = code;
    this.msg = msg;
  }
  public data: T;
  public code: number;
  public msg: string | null;
}
