-- Data for Name: access_counts; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.access_counts (id, count) FROM stdin;
\.
-- Name: access_counts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.access_counts_id_seq', 1, false);
-- Data for Name: img_types; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.img_types (id, code, name, status, created_at, updated_at, updated_by) FROM stdin;
1	DEFAULT	Mặc định	ACTIVE	2025-12-17 06:31:14.11744+00	\N	\N
2	OTHER	Khác	ACTIVE	2025-12-17 06:31:14.11744+00	\N	\N
3	DETAIL	Chi tiết	ACTIVE	2025-12-17 06:31:14.11744+00	\N	\N
4	COLOR	Màu sắc	ACTIVE	2025-12-17 06:31:14.11744+00	\N	\N
5	ORDER_GROUP	Hình ảnh nhóm đơn hàng	ACTIVE	2025-12-17 06:31:14.11744+00	\N	\N
6	ORDER_ITEM	Hình ảnh chi tiết đơn hàng	ACTIVE	2025-12-17 06:31:14.11744+00	\N	\N
\.
-- Name: img_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.img_types_id_seq', 6, true);
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.users (id, username, password, name, email, phone, status, role, cart_id, order_id, created_at, updated_at) FROM stdin;
1	phung	$2a$10$FweYQEWrVzF41AeEsSvkWuqmobbY6KbGdEp.NBg.Qhl02EWedPyEG	phung	admin@gmail.com	\N	ACTIVE	ADMIN	\N	\N	2025-12-17 06:31:32.096882+00	2025-12-17 06:31:32.096882+00
\.
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.users_id_seq', 1, true);
-- Data for Name: imgs; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.imgs (id, priority, url, public_id, title, subtitle, is_default, product_id, img_type_id, order_group_id, order_item_id, created_at, updated_at) FROM stdin;
1	1	https://example.com/images/rem-tranh-noren.jpg	rem-tranh-noren	Rèm – Tranh Noren	Category image	t	\N	1	\N	\N	2025-12-17 06:31:14.11744+00	\N
2	2	https://example.com/images/co-nobori-yatai.jpg	co-nobori-yatai	Cờ – Nobori – Yatai	Category image	t	\N	1	\N	\N	2025-12-17 06:31:14.11744+00	\N
3	3	https://example.com/images/ema-the-go-bang-ten.jpg	ema-the-go-bang-ten	Ema – Thẻ Gỗ – Bảng Tên	Category image	t	\N	1	\N	\N	2025-12-17 06:31:14.11744+00	\N
4	4	https://example.com/images/trang-tri-izakaya.jpg	trang-tri-izakaya	Trang Trí Izakaya	Category image	t	\N	1	\N	\N	2025-12-17 06:31:14.11744+00	\N
5	5	https://example.com/images/decor-truyen-thong-nhat.jpg	decor-truyen-thong-nhat	Decor Truyền Thống Nhật	Category image	t	\N	1	\N	\N	2025-12-17 06:31:14.11744+00	\N
7	0	https://res.cloudinary.com/cloudinarymen/image/upload/v1765953703/makotodecor/products/qsjknow7ydfmegva1za5.png	makotodecor/products/qsjknow7ydfmegva1za5	\N	\N	t	\N	1	\N	\N	2025-12-17 06:41:58.344618+00	2025-12-17 06:41:58.344618+00
6	0	https://res.cloudinary.com/cloudinarymen/image/upload/v1765953644/makotodecor/products/abzywfwclgds81mfmf9x.png	makotodecor/products/abzywfwclgds81mfmf9x	\N	\N	t	\N	1	\N	\N	2025-12-17 06:41:15.445877+00	2025-12-17 06:41:15.445877+00
\.
-- Name: imgs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.imgs_id_seq', 7, true);
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.categories (id, code, name, status, img_id, created_at, updated_at, updated_by) FROM stdin;
1	REM	Rèm – Tranh Noren	ACTIVE	1	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
2	CO	Cờ – Nobori – Yatai	ACTIVE	2	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
3	THE_GO	Ema – Thẻ Gỗ – Bảng Tên	ACTIVE	3	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
4	IZAKAYA	Trang Trí Izakaya	ACTIVE	4	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
5	DECOR_TRADITIONAL	Decor Truyền Thống Nhật	ACTIVE	5	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
\.
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.categories_id_seq', 5, true);
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.products (id, name, description, discount, sold, base_sold, status, category_id, created_at, updated_at, updated_by) FROM stdin;
1	Sản phẩm 1	\N	10	0	1000	ACTIVE	1	2025-12-17 06:41:14.059998+00	2025-12-17 06:41:15.730715+00	\N
2	Sản phẩm 2	\N	20	0	1000	ACTIVE	2	2025-12-17 06:41:57.512821+00	2025-12-17 06:41:58.622906+00	\N
\.
-- Fix circular reference: Update imgs.product_id after products are inserted
UPDATE public.imgs SET product_id = 2 WHERE id IN (7);
UPDATE public.imgs SET product_id = 1 WHERE id IN (6);
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.products_id_seq', 2, true);
-- Data for Name: sizes; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.sizes (id, size, price, product_id, is_active) FROM stdin;
1	m	100000	1	t
2	l	200000	1	t
3	M	100000	2	t
4	L	200000	2	f
\.
-- Name: sizes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.sizes_id_seq', 4, true);
-- Data for Name: colors; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.colors (id, name, color, img_id, product_id, is_active) FROM stdin;
1	Đen	#000000	\N	1	t
2	Đỏ	#c12f2f	\N	1	t
\.
-- Name: colors_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.colors_id_seq', 2, true);
-- Data for Name: carts; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.carts (id, user_id) FROM stdin;
1	1
\.
-- Name: carts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.carts_id_seq', 1, true);
-- Data for Name: cart_items; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.cart_items (id, cart_id, quantity, price, discount, product_id, size_id, color_id, created_at, updated_at) FROM stdin;
1	1	1	100000	20	2	3	\N	2025-12-17 07:33:49.209983+00	2025-12-17 07:33:49.209983+00
2	1	1	100000	10	1	1	1	2025-12-17 07:33:53.489479+00	2025-12-17 07:33:53.489479+00
3	1	1	200000	10	1	2	2	2025-12-17 07:40:52.370686+00	2025-12-17 07:40:52.370686+00
\.
-- Name: cart_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.cart_items_id_seq', 3, true);
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.orders (id, code, user_id, status, shipping_full_name, shipping_phone, shipping_address, shipping_note, payment_proof_url, payment_proof_public_id, total_price, created_at, updated_at, updated_by) FROM stdin;
1	ORD-f80840fc-9525-43c7-9f0f-ebe24267f7fc	1	NEW	dfgsd	112312	1212312	12123	\N	\N	340000	2025-12-17 07:34:19.595889+00	2025-12-17 07:34:21.924494+00	\N
\.
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.orders_id_seq', 2, true);
-- Data for Name: order_groups; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.order_groups (id, order_id, product_id, product_name, created_at) FROM stdin;
1	1	1	Sản phẩm 1	2025-12-17 07:34:20.181378+00
2	1	2	Sản phẩm 2	2025-12-17 07:34:21.230181+00
\.
-- Name: order_groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.order_groups_id_seq', 2, true);
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: neondb_owner
COPY public.order_items (id, order_id, quantity, price, discount, product_id, color_name, size_name, size_price, order_group_id) FROM stdin;
1	1	1	100000	10	1	Đen	m	100000	1
2	1	1	100000	20	2	\N	M	100000	2
3	1	1	100000	30	1	Đỏ	L	100000	1
\.
-- Name: order_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
SELECT pg_catalog.setval('public.order_items_id_seq', 3, true);
--
-- PostgreSQL database dump complete (for Flyway flow)
-- flyway_schema_history NOT included
--
