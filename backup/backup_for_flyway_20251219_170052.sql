
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
8	1	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022107/makotodecor/checkout/n4sfpofiotw090ed26ho.png	makotodecor/checkout/n4sfpofiotw090ed26ho	\N	\N	t	\N	5	\N	\N	2025-12-18 01:43:25.265065+00	2025-12-18 01:43:25.265065+00
9	2	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022108/makotodecor/checkout/zrsld2ofgacnyrmnmjdl.png	makotodecor/checkout/zrsld2ofgacnyrmnmjdl	\N	\N	f	\N	5	\N	\N	2025-12-18 01:43:25.533837+00	2025-12-18 01:43:25.533837+00
13	1	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022163/makotodecor/checkout/reymlnnescerfs2kslvw.png	makotodecor/checkout/reymlnnescerfs2kslvw	\N	\N	t	\N	5	\N	\N	2025-12-18 01:43:26.591269+00	2025-12-18 01:43:26.591269+00
14	2	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022165/makotodecor/checkout/sqn6ey1yw7aupf8e2njz.png	makotodecor/checkout/sqn6ey1yw7aupf8e2njz	\N	\N	f	\N	5	\N	\N	2025-12-18 01:43:27.132785+00	2025-12-18 01:43:27.132785+00
10	1	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022144/makotodecor/checkout/jbvebcjksaghjonvi12t.png	makotodecor/checkout/jbvebcjksaghjonvi12t	\N	\N	t	\N	6	\N	\N	2025-12-18 01:43:25.798556+00	2025-12-18 01:43:25.798556+00
11	2	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022145/makotodecor/checkout/pjteyzjrt8yr3q4uwasx.png	makotodecor/checkout/pjteyzjrt8yr3q4uwasx	\N	\N	f	\N	6	\N	\N	2025-12-18 01:43:26.06242+00	2025-12-18 01:43:26.06242+00
12	1	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022153/makotodecor/checkout/pfqobch99giq8ehao3gd.png	makotodecor/checkout/pfqobch99giq8ehao3gd	\N	\N	t	\N	6	\N	\N	2025-12-18 01:43:26.326149+00	2025-12-18 01:43:26.326149+00
15	1	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022172/makotodecor/checkout/o7nrr5de32tfapxxw60i.png	makotodecor/checkout/o7nrr5de32tfapxxw60i	\N	\N	t	\N	6	\N	\N	2025-12-18 01:43:27.658235+00	2025-12-18 01:43:27.658235+00
16	0	https://res.cloudinary.com/cloudinarymen/image/upload/v1765953644/makotodecor/products/abzywfwclgds81mfmf9x.png	makotodecor/products/abzywfwclgds81mfmf9x	\N	\N	t	\N	1	\N	\N	2025-12-19 07:05:25.399666+00	2025-12-19 07:05:25.399666+00
17	0	https://res.cloudinary.com/cloudinarymen/image/upload/v1765953703/makotodecor/products/qsjknow7ydfmegva1za5.png	makotodecor/products/qsjknow7ydfmegva1za5	\N	\N	t	\N	1	\N	\N	2025-12-19 07:22:38.987329+00	2025-12-19 07:22:38.987329+00
18	0	https://res.cloudinary.com/cloudinarymen/image/upload/v1766129045/makotodecor/categories/ttow1pe5ciwebg7ug82e.png	\N	\N	\N	f	\N	\N	\N	\N	2025-12-19 07:24:08.50392+00	2025-12-19 07:24:08.50392+00
\.

-- Name: imgs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner

SELECT pg_catalog.setval('public.imgs_id_seq', 18, true);

-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: neondb_owner

COPY public.categories (id, code, name, status, img_id, created_at, updated_at, updated_by) FROM stdin;
2	CO	Cờ – Nobori – Yatai	ACTIVE	2	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
3	THE_GO	Ema – Thẻ Gỗ – Bảng Tên	ACTIVE	3	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
4	IZAKAYA	Trang Trí Izakaya	ACTIVE	4	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
5	DECOR_TRADITIONAL	Decor Truyền Thống Nhật	ACTIVE	5	2025-12-17 06:31:14.11744+00	2025-12-17 06:31:14.11744+00	\N
1	REM	Rèm – Tranh Norenn	ACTIVE	18	2025-12-17 06:31:14.11744+00	2025-12-19 07:24:08.743485+00	\N
\.

-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner

SELECT pg_catalog.setval('public.categories_id_seq', 5, true);

-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: neondb_owner

COPY public.products (id, name, description, discount, sold, base_sold, status, category_id, created_at, updated_at, updated_by) FROM stdin;
1	Sản phẩm 1	\N	0	0	1000	ACTIVE	1	2025-12-17 06:41:14.059998+00	2025-12-19 07:05:21.706885+00	\N
2	Sản phẩm 2	\N	10	0	1000	ACTIVE	2	2025-12-17 06:41:57.512821+00	2025-12-19 07:22:36.733538+00	\N
\.

-- Fix FK dependency: Update imgs.product_id after products are inserted
UPDATE public.imgs SET product_id = 2 WHERE id IN (17);
UPDATE public.imgs SET product_id = 1 WHERE id IN (16);


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
3	1	1	200000	10	1	2	2	2025-12-17 07:40:52.370686+00	2025-12-17 07:40:52.370686+00
2	1	2	100000	10	1	1	1	2025-12-17 07:33:53.489479+00	2025-12-18 01:42:09.291553+00
\.

-- Name: cart_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner

SELECT pg_catalog.setval('public.cart_items_id_seq', 3, true);

-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: neondb_owner

COPY public.orders (id, code, user_id, status, shipping_full_name, shipping_phone, shipping_address, shipping_note, shipping_facebook_link, payment_proof_url, payment_proof_public_id, total_price, product_count, total_quantity, deposit_amount, remaining_amount, created_at, updated_at, updated_by) FROM stdin;
1	ORD-f80840fc-9525-43c7-9f0f-ebe24267f7fc	1	NEW	dfgsd	112312	1212312	12123	\N	\N	\N	340000	\N	\N	\N	\N	2025-12-17 07:34:19.595889+00	2025-12-17 07:34:21.924494+00	\N
3	ORD-5902a646-9bf5-4fa2-bb52-5572a67cd41e	1	NEW	dfg	dfgdfg	dfgdfg	\N	\N	https://res.cloudinary.com/cloudinarymen/image/upload/v1766022186/makotodecor/checkout/payment-proof/ltqdlnou8rh0goktpyeh.png	\N	880000	\N	\N	\N	\N	2025-12-18 01:43:23.112161+00	2025-12-18 01:43:28.186708+00	\N
4	25121915214323	1	DEPOSITED	dfgdfg	dfgdfg	dfgdfg	\N	\N	https://res.cloudinary.com/cloudinarymen/image/upload/v1766132522/makotodecor/orders/payment-proof/ixdescqawunqzg4ahvyj.png	makotodecor/orders/payment-proof/ixdescqawunqzg4ahvyj	960000	2	4	288000	672000	2025-12-19 08:21:43.79285+00	2025-12-19 08:23:27.849274+00	\N
5	25121915482143	1	PENDING_DEPOSIT	hfgh	fghfg	hfgfghfgh	\N	\N	\N	\N	980000	2	4	294000	686000	2025-12-19 08:48:21.636548+00	2025-12-19 08:48:23.577802+00	\N
6	25121915565118	1	PENDING_DEPOSIT	pôp]	po[ươp]ơ	pơ]ôp[	\N	\N	\N	\N	980000	2	4	294000	686000	2025-12-19 08:56:51.44314+00	2025-12-19 08:56:53.609483+00	\N
\.

-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner

SELECT pg_catalog.setval('public.orders_id_seq', 6, true);

-- Data for Name: order_groups; Type: TABLE DATA; Schema: public; Owner: neondb_owner

COPY public.order_groups (id, order_id, product_id, product_name, total_quantity, created_at) FROM stdin;
1	1	1	Sản phẩm 1	\N	2025-12-17 07:34:20.181378+00
2	1	2	Sản phẩm 2	\N	2025-12-17 07:34:21.230181+00
3	3	1	Sản phẩm 1	\N	2025-12-18 01:43:23.678068+00
4	3	2	Sản phẩm 2	\N	2025-12-18 01:43:24.736893+00
5	4	1	Sản phẩm 1	3	2025-12-19 08:21:44.535613+00
6	4	2	Sản phẩm 2	1	2025-12-19 08:21:45.717973+00
7	5	1	Sản phẩm 1	3	2025-12-19 08:48:22.130402+00
8	5	2	Sản phẩm 2	1	2025-12-19 08:48:23.094815+00
9	6	1	Sản phẩm 1	3	2025-12-19 08:56:51.986083+00
10	6	2	Sản phẩm 2	1	2025-12-19 08:56:53.066426+00
\.

-- Fix FK dependency: Update imgs.order_group_id after order_groups are inserted
UPDATE public.imgs SET order_group_id = 4 WHERE id IN (14, 13);
UPDATE public.imgs SET order_group_id = 3 WHERE id IN (9, 8);


-- Name: order_groups_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner

SELECT pg_catalog.setval('public.order_groups_id_seq', 10, true);

-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: neondb_owner

COPY public.order_items (id, order_id, quantity, price, discount, product_id, color_name, size_name, size_price, order_group_id) FROM stdin;
1	1	1	100000	10	1	Đen	m	100000	1
2	1	1	100000	20	2	\N	M	100000	2
3	1	1	100000	30	1	Đỏ	L	100000	1
4	3	2	100000	10	1	Đen	m	100000	3
5	3	1	200000	10	1	Đỏ	l	200000	3
6	3	1	100000	20	2	\N	M	100000	4
7	4	2	100000	0	1	Đen	m	100000	5
8	4	1	200000	0	1	Đỏ	l	200000	5
9	4	1	100000	20	2	\N	M	100000	6
10	5	2	100000	0	1	Đen	m	100000	7
11	5	1	200000	0	1	Đỏ	l	200000	7
12	5	1	100000	10	2	\N	M	100000	8
13	6	2	100000	0	1	Đen	m	100000	9
14	6	1	200000	0	1	Đỏ	l	200000	9
15	6	1	100000	10	2	\N	M	100000	10
\.

-- Fix FK dependency: Update imgs.order_item_id after order_items are inserted
UPDATE public.imgs SET order_item_id = 5 WHERE id IN (12);
UPDATE public.imgs SET order_item_id = 6 WHERE id IN (15);
UPDATE public.imgs SET order_item_id = 4 WHERE id IN (10, 11);


-- Name: order_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner

SELECT pg_catalog.setval('public.order_items_id_seq', 15, true);

--
-- PostgreSQL database dump complete (for Flyway flow)
-- flyway_schema_history NOT included
--
