import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBlog } from '@/shared/model/blog/blog.model';
import BlogService from './blog.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class BlogDetails extends Vue {
  @Inject('blogService') private blogService: () => BlogService;
  @Inject('alertService') private alertService: () => AlertService;

  public blog: IBlog = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.blogId) {
        vm.retrieveBlog(to.params.blogId);
      }
    });
  }

  public retrieveBlog(blogId) {
    this.blogService()
      .find(blogId)
      .then(res => {
        this.blog = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
