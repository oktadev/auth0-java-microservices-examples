import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Blog = () => import('@/entities/blog/blog/blog.vue');
const BlogUpdate = () => import('@/entities/blog/blog/blog-update.vue');
const BlogDetails = () => import('@/entities/blog/blog/blog-details.vue');

const Post = () => import('@/entities/blog/post/post.vue');
const PostUpdate = () => import('@/entities/blog/post/post-update.vue');
const PostDetails = () => import('@/entities/blog/post/post-details.vue');

const Tag = () => import('@/entities/blog/tag/tag.vue');
const TagUpdate = () => import('@/entities/blog/tag/tag-update.vue');
const TagDetails = () => import('@/entities/blog/tag/tag-details.vue');

const Product = () => import('@/entities/store/product/product.vue');
const ProductUpdate = () => import('@/entities/store/product/product-update.vue');
const ProductDetails = () => import('@/entities/store/product/product-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'blog',
      name: 'Blog',
      component: Blog,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'blog/new',
      name: 'BlogCreate',
      component: BlogUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'blog/:blogId/edit',
      name: 'BlogEdit',
      component: BlogUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'blog/:blogId/view',
      name: 'BlogView',
      component: BlogDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'post',
      name: 'Post',
      component: Post,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'post/new',
      name: 'PostCreate',
      component: PostUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'post/:postId/edit',
      name: 'PostEdit',
      component: PostUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'post/:postId/view',
      name: 'PostView',
      component: PostDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag',
      name: 'Tag',
      component: Tag,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/new',
      name: 'TagCreate',
      component: TagUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/:tagId/edit',
      name: 'TagEdit',
      component: TagUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/:tagId/view',
      name: 'TagView',
      component: TagDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product',
      name: 'Product',
      component: Product,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/new',
      name: 'ProductCreate',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/edit',
      name: 'ProductEdit',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/view',
      name: 'ProductView',
      component: ProductDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
